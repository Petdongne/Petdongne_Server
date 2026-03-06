###############################################################
# modules/rds/main.tf — 기존 PostgreSQL RDS import 관리
#
# ⚠️  apply 전에 반드시 import 먼저 실행하세요:
#
#   terraform import module.rds.aws_db_instance.main \
#     <your-rds-identifier>
#
# RDS 식별자 확인:
#   aws rds describe-db-instances \
#     --query 'DBInstances[*].[DBInstanceIdentifier,Endpoint.Address]'
###############################################################

resource "aws_db_instance" "main" {
  identifier = var.db_identifier

  engine         = "postgres"
  engine_version = "17"          # 실제 버전: 17.6 (마이너 버전은 ignore_changes로 무시)
  instance_class = "db.t4g.micro" # 실제 인스턴스 타입

  username = var.db_username
  # password는 Terraform 관리에서 제외 (Secrets Manager에서 관리)
  manage_master_user_password = false

  # 네트워크
  db_subnet_group_name   = aws_db_subnet_group.main.name
  vpc_security_group_ids = [var.sg_rds_id]
  publicly_accessible    = false  # 프라이빗 접근만 허용

  # 스토리지
  allocated_storage     = 20    # 실제 값으로 변경
  max_allocated_storage = 100   # 자동 확장 상한
  storage_type          = "gp3"
  storage_encrypted     = true

  # 백업
  backup_retention_period = 1
  backup_window           = "03:00-04:00"  # UTC (한국 12:00-13:00)
  maintenance_window      = "Mon:04:00-Mon:05:00"

  # 모니터링 — Enhanced Monitoring, Performance Insights 비활성화 (과금 방지)
  monitoring_interval             = 0     # Enhanced Monitoring 비활성화
  performance_insights_enabled    = false # Performance Insights 비활성화

  # 삭제 보호 — Terraform destroy 실수 방지
  deletion_protection = true
  skip_final_snapshot = false
  final_snapshot_identifier = "${var.project_name}-${var.env}-final-snapshot"

  # import 후 Terraform이 모르는 설정 변경을 무시
  lifecycle {
    prevent_destroy = true   # terraform destroy 완전 차단
    ignore_changes  = [
      password,                 # 패스워드는 Secrets Manager에서 관리
      engine_version,           # 마이너 버전 자동 업그레이드 무시
      db_name,                  # 기존 RDS 생성 시 DB 이름 미지정 → 리소스 교체 방지를 위해 무시
      username                 # 실제 AWS 값(postgres)과 변수 값이 달라 교체 방지
    ]
  }

  tags = { Name = "${var.project_name}-${var.env}-postgres" }
}

# ── DB 서브넷 그룹 ─────────────────────────────────────────────
resource "aws_db_subnet_group" "main" {
  name        = "${var.project_name}-${var.env}-db-subnet-group"
  subnet_ids  = var.private_subnet_ids
  description = "PostgreSQL RDS subnet group"

  tags = { Name = "${var.project_name}-${var.env}-db-subnet-group" }
}
