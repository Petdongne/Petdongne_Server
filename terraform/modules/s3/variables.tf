variable "project_name" {
  description = "프로젝트 이름"
  type        = string
}

variable "env" {
  description = "배포 환경 (dev / prod)"
  type        = string
}

variable "aws_account_id" {
  description = "AWS 계정 ID"
  type        = string
}

variable "ecs_task_role_arn" {
  description = "ECS Task Role ARN (S3 접근 권한 부여 대상)"
  type        = string
}
