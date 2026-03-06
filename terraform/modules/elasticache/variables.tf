variable "project_name" {
  description = "프로젝트 이름"
  type        = string
}

variable "env" {
  description = "배포 환경 (dev / prod)"
  type        = string
}

variable "private_subnet_ids" {
  description = "프라이빗 서브넷 ID 목록"
  type        = list(string)
}

variable "sg_elasticache_id" {
  description = "ElastiCache 보안그룹 ID"
  type        = string
}
