###############################################################
# outputs.tf — 주요 리소스 ARN / ID 출력
###############################################################

output "vpc_id" {
  value = module.vpc.vpc_id
}

output "alb_dns_name" {
  description = "ALB DNS 주소 (앱 접속 URL)"
  value       = module.ecs.alb_dns_name
}

output "ecr_repository_url" {
  description = "ECR 이미지 주소 (GitHub Actions에서 사용)"
  value       = module.ecr.repository_url
}

output "ecs_cluster_name" {
  value = module.ecs.cluster_name
}

output "ecs_service_name" {
  value = module.ecs.service_name
}

output "github_actions_role_arn" {
  description = "GitHub Actions OIDC Role ARN → GitHub Secret에 등록"
  value       = module.iam.github_actions_role_arn
}

output "elasticache_endpoint" {
  description = "Valkey 접속 엔드포인트"
  value       = module.elasticache.endpoint
}

output "s3_bucket_name" {
  description = "S3 버킷명"
  value       = module.s3.bucket_name
}

output "rds_endpoint" {
  description = "RDS PostgreSQL 엔드포인트"
  value       = module.rds.endpoint
}
