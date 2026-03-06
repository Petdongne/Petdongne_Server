###############################################################
# variables.tf — 프로젝트 전역 변수
###############################################################

variable "project_name" {
  description = "프로젝트 이름 (리소스 네이밍에 사용)"
  type        = string
  default     = "petdongne-server"   # ← 본인 프로젝트명으로 변경
}

variable "env" {
  description = "배포 환경 (prod / dev)"
  type        = string
  default     = "prod"
}

variable "aws_region" {
  description = "AWS 리전"
  type        = string
  default     = "ap-northeast-2"  # 서울
}

variable "aws_account_id" {
  description = "AWS 계정 ID (12자리 숫자)"
  type        = string
}

# ─────────────────────────────────────────────────
# VPC / 네트워크
# ─────────────────────────────────────────────────
variable "vpc_cidr" {
  type    = string
  default = "10.0.0.0/16"
}

variable "azs" {
  description = "사용할 AZ 목록"
  type        = list(string)
  default     = ["ap-northeast-2a", "ap-northeast-2c"]
}

variable "public_subnet_cidrs" {
  description = "ALB용 퍼블릭 서브넷 (AZ 순서와 일치)"
  type        = list(string)
  default     = ["10.0.1.0/24", "10.0.2.0/24"]
}

variable "private_subnet_cidrs" {
  description = "ECS Fargate용 프라이빗 서브넷"
  type        = list(string)
  default     = ["10.0.11.0/24", "10.0.12.0/24"]
}

# ─────────────────────────────────────────────────
# GitHub OIDC (IAM)
# ─────────────────────────────────────────────────
variable "github_org" {
  description = "GitHub 조직 또는 사용자명"
  type        = string
  # 예: "my-org" 또는 "my-username"
}

variable "github_repo" {
  description = "GitHub 레포지토리 이름"
  type        = string
  # 예: "my-spring-app"
}

# ─────────────────────────────────────────────────
# ECS 컨테이너 설정
# ─────────────────────────────────────────────────
variable "container_port" {
  description = "Spring Boot 앱 포트 (기본 8080)"
  type        = number
  default     = 8080
}

variable "task_cpu" {
  description = "Fargate Task CPU 단위 (256=0.25vCPU)"
  type        = number
}

variable "task_memory" {
  description = "Fargate Task 메모리(MiB)"
  type        = number
}

variable "desired_count" {
  description = "ECS 서비스 실행 Task 수"
  type        = number
  default     = 1
}

# ─────────────────────────────────────────────────
# RDS
# ─────────────────────────────────────────────────
variable "db_identifier" {
  description = "RDS 인스턴스 식별자"
  type        = string
}

variable "db_name" {
  description = "PostgreSQL 데이터베이스 이름"
  type        = string
}

variable "db_username" {
  description = "PostgreSQL 마스터 사용자명"
  type        = string
}
