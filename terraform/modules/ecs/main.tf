###############################################################
# modules/ecs/main.tf — ALB + ECS Cluster/Service (롤링 배포)
###############################################################

# ── Application Load Balancer ─────────────────────────────────
resource "aws_lb" "main" {
  name               = "${var.project_name}-${var.env}-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [var.sg_alb_id]
  subnets            = var.public_subnet_ids

  tags = { Name = "${var.project_name}-${var.env}-alb" }
}

resource "aws_lb_target_group" "app" {
  name        = "${var.project_name}-${var.env}-tg"
  port        = var.container_port
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"   # Fargate는 반드시 ip 타입이어야함

  health_check {
    path                = "/actuator/health"   # Spring Actuator 엔드포인트
    port                = "9090"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 3
    matcher             = "200"
  }

  tags = { Name = "${var.project_name}-${var.env}-tg" }
}

resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.main.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app.arn
  }

  # HTTPS 적용 시: 아래로 교체 (ACM 인증서 ARN 필요)
  # default_action {
  #   type = "redirect"
  #   redirect {
  #     port        = "443"
  #     protocol    = "HTTPS"
  #     status_code = "HTTP_301"
  #   }
  # }
}

# ── CloudWatch 로그 그룹 ──────────────────────────────────────
resource "aws_cloudwatch_log_group" "app" {
  name              = "/ecs/${var.project_name}-${var.env}"
  retention_in_days = 30

  tags = { Name = "${var.project_name}-${var.env}-logs" }
}

# ── ECS 클러스터 ──────────────────────────────────────────────
resource "aws_ecs_cluster" "main" {
  name = "${var.project_name}-${var.env}-cluster"

  setting {
    name  = "containerInsights"
    value = "enabled"   # CloudWatch Container Insights 활성화
  }

  tags = { Name = "${var.project_name}-${var.env}-cluster" }
}

# ── Task Definition ───────────────────────────────────────────
resource "aws_ecs_task_definition" "app" {
  family                   = "${var.project_name}-${var.env}"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = var.task_cpu
  memory                   = var.task_memory
  execution_role_arn       = var.task_execution_role_arn
  task_role_arn            = var.task_role_arn

  container_definitions = jsonencode([{
    name  = "${var.project_name}-app"
    image = "${var.ecr_repository_url}:latest"

    portMappings = [
      {
        containerPort = var.container_port
        protocol      = "tcp"
      },
      {
        containerPort = 9090  # actuator
        protocol      = "tcp"
      }
    ]

    # Secrets Manager → 환경변수 주입
    secrets = [
      {
        name      = "SPRING_DATASOURCE_URL"
        valueFrom = "${var.secrets_arn}:SPRING_DATASOURCE_URL::"
      },
      {
        name      = "SPRING_DATASOURCE_USERNAME"
        valueFrom = "${var.secrets_arn}:SPRING_DATASOURCE_USERNAME::"
      },
      {
        name      = "SPRING_DATASOURCE_PASSWORD"
        valueFrom = "${var.secrets_arn}:SPRING_DATASOURCE_PASSWORD::"
      },
      {
        name      = "SPRING_PROFILES_ACTIVE"
        valueFrom = "${var.secrets_arn}:SPRING_PROFILES_ACTIVE::"
      },
      {
        name      = "APP_FRONTEND_URL"
        valueFrom = "${var.secrets_arn}:APP_FRONTEND_URL::"
      },
      {
        name      = "KAKAO_CLIENT_ID"
        valueFrom = "${var.secrets_arn}:KAKAO_CLIENT_ID::"
      },
      {
        name      = "KAKAO_CLIENT_SECRET"
        valueFrom = "${var.secrets_arn}:KAKAO_CLIENT_SECRET::"
      },
      {
        name      = "KAKAO_REDIRECT_URI"
        valueFrom = "${var.secrets_arn}:KAKAO_REDIRECT_URI::"
      }
    ]

    # 비민감 설정값은 environment로 직접 주입
    environment = [
      {
        name  = "SPRING_DATA_REDIS_HOST"
        value = var.elasticache_endpoint
      },
      {
        name  = "SPRING_DATA_REDIS_PORT"
        value = tostring(var.elasticache_port)
      },
      {
        name  = "BUCKET_NAME"
        value = var.s3_bucket_name
      },
      {
        name  = "AWS_REGION"
        value = var.aws_region
      },
      {
        name  = "ACTUATOR_PORT"
        value = "9090"
      },
      {
        name  = "ACTUATOR_EXPOSURE_ENDPOINT"
        value = "health,prometheus"
      },
      {
        name  = "APP_COOKIE_SECURE"
        value = "true"
      },
      {
        name  = "REDIS_SSL"
        value = "true"
      }
    ]

    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = aws_cloudwatch_log_group.app.name
        "awslogs-region"        = var.aws_region
        "awslogs-stream-prefix" = "ecs"
      }
    }

    # 헬스체크 (컨테이너 레벨)
    healthCheck = {
      command     = ["CMD-SHELL", "wget -qO- http://localhost:9090/actuator/health || exit 1"]
      interval    = 30
      timeout     = 5
      retries     = 3
      startPeriod = 60   # Spring Boot 기동 시간 여유
    }
  }])

  tags = { Name = "${var.project_name}-${var.env}-task" }
}

# ── ECS 서비스 (롤링 배포) ────────────────────────────────────
resource "aws_ecs_service" "app" {
  name            = "${var.project_name}-${var.env}-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = var.desired_count
  launch_type     = "FARGATE"

  # 롤링 배포 설정
  deployment_minimum_healthy_percent = 100
  deployment_maximum_percent         = 200
  health_check_grace_period_seconds = 120

  network_configuration {
    subnets          = var.public_subnet_ids  # NAT Gateway 미사용으로 퍼블릭 서브넷 배치 (Kakao OAuth2 등 외부 인터넷 접근 필요)
    security_groups  = [var.sg_ecs_id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.app.arn
    container_name   = "${var.project_name}-app"
    container_port   = var.container_port
  }

  # GitHub Actions가 Task Definition을 업데이트하므로
  # Terraform이 task_definition 변경을 무시하도록 설정
  lifecycle {
    ignore_changes = [task_definition]
  }

  depends_on = [aws_lb_listener.http]

  tags = { Name = "${var.project_name}-${var.env}-service" }
}
