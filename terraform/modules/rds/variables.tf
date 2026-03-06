variable "project_name" {
  description = "프로젝트 이름"
  type        = string
}

variable "env" {
  description = "배포 환경 (dev / prod)"
  type        = string
}

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

variable "sg_rds_id" {
  description = "RDS 보안그룹 ID"
  type        = string
}

variable "private_subnet_ids" {
  description = "프라이빗 서브넷 ID 목록"
  type        = list(string)
}
