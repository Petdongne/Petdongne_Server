variable "project_name" {
  description = "프로젝트 이름"
  type        = string
}

variable "env" {
  description = "배포 환경 (dev / prod)"
  type        = string
}

variable "vpc_id" {
  description = "VPC ID"
  type        = string
}

variable "aws_region" {
  description = "AWS 리전"
  type        = string
}

variable "private_route_table_id" {
  description = "프라이빗 라우트 테이블 ID (S3 Gateway 엔드포인트용)"
  type        = string
}

variable "public_route_table_id" {
  description = "퍼블릭 라우트 테이블 ID (S3 Gateway 엔드포인트용)"
  type        = string
}

