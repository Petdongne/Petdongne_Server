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

variable "vpc_id" {
  description = "VPC ID"
  type        = string
}

variable "public_subnet_ids" {
  description = "퍼블릭 서브넷 ID 목록 (ALB용)"
  type        = list(string)
}

variable "private_subnet_ids" {
  description = "프라이빗 서브넷 ID 목록 (ECS Fargate용)"
  type        = list(string)
}

variable "sg_alb_id" {
  description = "ALB 보안그룹 ID"
  type        = string
}

variable "sg_ecs_id" {
  description = "ECS 보안그룹 ID"
  type        = string
}

variable "ecr_repository_url" {
  description = "ECR 레포지토리 URL"
  type        = string
}

variable "task_execution_role_arn" {
  description = "ECS Task Execution Role ARN"
  type        = string
}

variable "task_role_arn" {
  description = "ECS Task Role ARN"
  type        = string
}

variable "secrets_arn" {
  description = "Secrets Manager Secret ARN"
  type        = string
}

variable "elasticache_endpoint" {
  description = "ElastiCache 엔드포인트 주소"
  type        = string
}

variable "elasticache_port" {
  description = "ElastiCache 포트"
  type        = number
}

variable "s3_bucket_name" {
  description = "S3 버킷 이름"
  type        = string
}

variable "container_port" {
  description = "컨테이너 포트"
  type        = number
  default     = 8080
}

variable "task_cpu" {
  description = "Fargate Task CPU 단위"
  type        = number
  default     = 512
}

variable "task_memory" {
  description = "Fargate Task 메모리(MiB)"
  type        = number
  default     = 1024
}

variable "desired_count" {
  description = "ECS 서비스 실행 Task 수"
  type        = number
  default     = 1
}
