variable "project_name" {
  description = "프로젝트 이름"
  type        = string
}

variable "env" {
  description = "배포 환경 (dev / prod)"
  type        = string
}

variable "aws_region" {
  description = "AWS 리전"
  type        = string
}

variable "aws_account_id" {
  description = "AWS 계정 ID"
  type        = string
}

variable "github_org" {
  description = "GitHub 조직 또는 사용자명"
  type        = string
}

variable "github_repo" {
  description = "GitHub 레포지토리 이름"
  type        = string
}
