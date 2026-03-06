###############################################################
# modules/s3/main.tf — 애플리케이션용 S3 버킷
###############################################################

resource "aws_s3_bucket" "app" {
  bucket = "${var.project_name}-${var.env}-app-${var.aws_account_id}"
  # 버킷명은 전 세계 고유 → 계정 ID를 suffix로 사용

  tags = { Name = "${var.project_name}-${var.env}-app" }
}

# ── 퍼블릭 접근 완전 차단 ─────────────────────────────────────
resource "aws_s3_bucket_public_access_block" "app" {
  bucket = aws_s3_bucket.app.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

# ── 버전 관리 활성화 (파일 실수 삭제 복구용) ──────────────────
resource "aws_s3_bucket_versioning" "app" {
  bucket = aws_s3_bucket.app.id
  versioning_configuration {
    status = "Enabled"
  }
}

# ── 서버 사이드 암호화 (AES-256) ──────────────────────────────
resource "aws_s3_bucket_server_side_encryption_configuration" "app" {
  bucket = aws_s3_bucket.app.id

  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
  }
}

# ── 수명 주기: 오래된 버전 자동 삭제 (스토리지 비용 절감) ─────
resource "aws_s3_bucket_lifecycle_configuration" "app" {
  bucket = aws_s3_bucket.app.id

  rule {
    id     = "expire-old-versions"
    status = "Enabled"

    filter {}

    noncurrent_version_expiration {
      noncurrent_days = 30   # 이전 버전 30일 후 삭제
    }
  }
}

# ── ECS Task Role에 버킷 접근 권한 부여 ──────────────────────
resource "aws_s3_bucket_policy" "app" {
  bucket = aws_s3_bucket.app.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "AllowECSTaskAccess"
        Effect = "Allow"
        Principal = {
          AWS = var.ecs_task_role_arn
        }
        Action = [
          "s3:GetObject",
          "s3:PutObject",
          "s3:DeleteObject",
          "s3:ListBucket"
        ]
        Resource = [
          aws_s3_bucket.app.arn,
          "${aws_s3_bucket.app.arn}/*"
        ]
      }
    ]
  })
}
