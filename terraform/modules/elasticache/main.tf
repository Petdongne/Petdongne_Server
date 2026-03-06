###############################################################
# modules/elasticache/main.tf — ElastiCache Serverless (Valkey)
#
# Serverless 선택 이유:
#   - 노드 타입/클러스터 크기 사전 설정 불필요
#   - 사용량에 따라 자동 스케일
#   - 최소 비용: ~$0.0065/ECPU + $0.20/GB (사용한 만큼만)
###############################################################

resource "aws_elasticache_serverless_cache" "valkey" {
  name   = "${var.project_name}-${var.env}-valkey"
  engine = "valkey"

  # 캐시 용량 상한 설정 (비용 상한 제어)
  cache_usage_limits {
    data_storage {
      maximum = 5    # 최대 5GB (필요 시 조정)
      unit    = "GB"
    }
    ecpu_per_second {
      maximum = 5000  # 최대 ECPU/초
    }
  }

  # 프라이빗 서브넷에 배치
  subnet_ids         = var.private_subnet_ids
  security_group_ids = [var.sg_elasticache_id]

  # 자동 스냅샷 (일 1회, 1일 보관)
  snapshot_retention_limit = 1

  tags = { Name = "${var.project_name}-${var.env}-valkey" }
}
