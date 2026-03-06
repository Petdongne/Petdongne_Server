###############################################################
# modules/ecr/main.tf — ECR 레포지토리
###############################################################

resource "aws_ecr_repository" "app" {
  name                 = "${var.project_name}-${var.env}"
  image_tag_mutability = "MUTABLE"   # :latest 태그 덮어쓰기 허용

  image_scanning_configuration {
    scan_on_push = true   # 푸시 시 보안 취약점 스캔
  }

  tags = { Name = "${var.project_name}-${var.env}" }
}

# 오래된 이미지 자동 삭제 (최근 10개만 유지)
resource "aws_ecr_lifecycle_policy" "app" {
  repository = aws_ecr_repository.app.name

  policy = jsonencode({
    rules = [{
      rulePriority = 1
      description  = "최근 10개 이미지만 유지"
      selection = {
        tagStatus   = "any"
        countType   = "imageCountMoreThan"
        countNumber = 10
      }
      action = { type = "expire" }
    }]
  })
}
