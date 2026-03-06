###############################################################
# modules/secrets/main.tf — AWS Secrets Manager
###############################################################

resource "aws_secretsmanager_secret" "app" {
  name        = "${var.project_name}/${var.env}/app"
  description = "Spring Boot 애플리케이션 시크릿"

  recovery_window_in_days = 0

  tags = { Name = "${var.project_name}-${var.env}-secret" }
}

# 초기값 설정
resource "aws_secretsmanager_secret_version" "app" {
  secret_id = aws_secretsmanager_secret.app.id

  secret_string = jsonencode({
    SPRING_DATASOURCE_URL      = "jdbc:postgresql://<RDS_ENDPOINT>:5432/<DB_NAME>"
    SPRING_DATASOURCE_USERNAME = "dbuser"
    SPRING_DATASOURCE_PASSWORD = "CHANGE_ME"
    SPRING_PROFILES_ACTIVE     = "prod"
    APP_FRONTEND_URL           = "https://<FRONTEND_DOMAIN>"
    KAKAO_CLIENT_ID            = "CHANGE_ME"
    KAKAO_CLIENT_SECRET        = "CHANGE_ME"
    KAKAO_REDIRECT_URI         = "https://<DOMAIN>/login/oauth2/code/kakao"
  })

  # 실제 시크릿 값은 Terraform 외부에서 관리
  lifecycle {
    ignore_changes = [secret_string]
  }
}
