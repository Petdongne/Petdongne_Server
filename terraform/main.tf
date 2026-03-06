###############################################################
# main.tf — 전체 모듈 연결
###############################################################

module "vpc" {
  source = "./modules/vpc"

  project_name         = var.project_name
  env                  = var.env
  vpc_cidr             = var.vpc_cidr
  public_subnet_cidrs  = var.public_subnet_cidrs
  private_subnet_cidrs = var.private_subnet_cidrs
  azs                  = var.azs
}

module "security_groups" {
  source = "./modules/security-groups"

  project_name = var.project_name
  env          = var.env
  vpc_id       = module.vpc.vpc_id
}

# ── VPC 엔드포인트 ────────────────────────────────────────────
module "vpc_endpoints" {
  source = "./modules/vpc-endpoints"

  project_name           = var.project_name
  env                    = var.env
  aws_region             = var.aws_region
  vpc_id                 = module.vpc.vpc_id
  private_route_table_id = module.vpc.private_route_table_id
  public_route_table_id  = module.vpc.public_route_table_id
}

module "iam" {
  source = "./modules/iam"

  project_name   = var.project_name
  env            = var.env
  aws_region     = var.aws_region
  aws_account_id = var.aws_account_id
  github_org     = var.github_org
  github_repo    = var.github_repo
}

module "ecr" {
  source = "./modules/ecr"

  project_name = var.project_name
  env          = var.env
}

module "secrets" {
  source = "./modules/secrets"

  project_name = var.project_name
  env          = var.env
}

# ── S3 ────────────────────────────────────────────────────────
module "s3" {
  source = "./modules/s3"

  project_name      = var.project_name
  env               = var.env
  aws_account_id    = var.aws_account_id
  ecs_task_role_arn = module.iam.ecs_task_role_arn
}

# ── ElastiCache Valkey Serverless ─────────────────────────────
module "elasticache" {
  source = "./modules/elasticache"

  project_name       = var.project_name
  env                = var.env
  private_subnet_ids = module.vpc.private_subnet_ids
  sg_elasticache_id  = module.security_groups.sg_elasticache_id
}

# ── RDS PostgreSQL (기존 import) ───────────────────────────────
module "rds" {
  source = "./modules/rds"

  project_name       = var.project_name
  env                = var.env
  private_subnet_ids = module.vpc.private_subnet_ids
  sg_rds_id          = module.security_groups.sg_rds_id
  db_identifier      = var.db_identifier
  db_name            = var.db_name
  db_username        = var.db_username
}

# ── ECS (ALB + Fargate 서비스) ────────────────────────────────
module "ecs" {
  source = "./modules/ecs"

  project_name   = var.project_name
  env            = var.env
  aws_region     = var.aws_region
  aws_account_id = var.aws_account_id

  vpc_id             = module.vpc.vpc_id
  public_subnet_ids  = module.vpc.public_subnet_ids
  private_subnet_ids = module.vpc.private_subnet_ids

  sg_alb_id = module.security_groups.sg_alb_id
  sg_ecs_id = module.security_groups.sg_ecs_id

  ecr_repository_url      = module.ecr.repository_url
  task_execution_role_arn = module.iam.ecs_task_execution_role_arn
  task_role_arn           = module.iam.ecs_task_role_arn

  secrets_arn = module.secrets.secret_arn

  elasticache_endpoint = module.elasticache.endpoint
  elasticache_port     = module.elasticache.port
  s3_bucket_name       = module.s3.bucket_name

  container_port = var.container_port
  task_cpu       = var.task_cpu
  task_memory    = var.task_memory
  desired_count  = var.desired_count
}
