###############################################################
# modules/security-groups/main.tf
# ALB / ECS / RDS / ElastiCache 보안그룹
###############################################################

# ── ALB 보안 그룹 ─────────────────────────────────────────────
resource "aws_security_group" "alb" {
  name        = "${var.project_name}-${var.env}-sg-alb"
  description = "ALB: Allow HTTP/HTTPS from internet"
  vpc_id      = var.vpc_id

  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "HTTPS"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = { Name = "${var.project_name}-${var.env}-sg-alb" }
}

# ── ECS 보안 그룹 (ALB에서만 트래픽 수신) ─────────────────────
resource "aws_security_group" "ecs" {
  name        = "${var.project_name}-${var.env}-sg-ecs"
  description = "ECS Fargate: Allow traffic from ALB only"
  vpc_id      = var.vpc_id

  ingress {
    description     = "ALB to ECS (app)"
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id]
  }

  ingress {
    description     = "ALB to ECS (actuator)"
    from_port       = 9090
    to_port         = 9090
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id]
  }

  egress {
    description = "Allow all outbound (ECR pull, Secrets Manager, etc.)"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = { Name = "${var.project_name}-${var.env}-sg-ecs" }
}

# ── RDS 보안 그룹 (ECS에서만 PostgreSQL 접근 허용) ──────────────
resource "aws_security_group" "rds" {
  name        = "${var.project_name}-${var.env}-sg-rds"
  description = "RDS: ECS only"
  vpc_id      = var.vpc_id

  ingress {
    description     = "ECS to RDS (PostgreSQL)"
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.ecs.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = { Name = "${var.project_name}-${var.env}-sg-rds" }
}

# ── ElastiCache (Valkey Serverless) 보안그룹 ──────────────────
# Valkey 기본 포트 6379, ECS에서만 접근 허용
resource "aws_security_group" "elasticache" {
  name        = "${var.project_name}-${var.env}-sg-elasticache"
  description = "ElastiCache Valkey: Allow access from ECS only"
  vpc_id      = var.vpc_id

  ingress {
    description     = "ECS to Valkey"
    from_port       = 6379
    to_port         = 6379
    protocol        = "tcp"
    security_groups = [aws_security_group.ecs.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = { Name = "${var.project_name}-${var.env}-sg-elasticache" }
}
