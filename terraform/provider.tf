###############################################################
# provider.tf — AWS 공급자 + Terraform 백엔드 설정
###############################################################

terraform {
  required_version = ">= 1.6.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  # ── S3 원격 상태 저장 (권장) ──────────────────────────────
  # 처음 apply 전에 아래 S3 버킷 + DynamoDB 테이블을 먼저 수동 생성하세요.
  # 버킷명: {project_name}-terraform-state-{account_id}
  # DynamoDB: terraform-lock (PK: LockID)
  # 준비되면 아래 주석을 해제하세요.
  #
  # backend "s3" {
  #   bucket         = "myapp-terraform-state-123456789012"
  #   key            = "prod/terraform.tfstate"
  #   region         = "ap-northeast-2"
  #   dynamodb_table = "terraform-lock"
  #   encrypt        = true
  # }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project     = var.project_name
      Environment = var.env
      ManagedBy   = "terraform"
    }
  }
}
