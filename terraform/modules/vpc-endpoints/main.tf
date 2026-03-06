###############################################################
# modules/vpc-endpoints/main.tf
#
# Gateway 엔드포인트 (무료):
#   - S3: ECR 이미지 레이어가 S3에 저장되므로 필수
#         퍼블릭/프라이빗 서브넷 모두 적용
#
# Interface 엔드포인트는 ECS가 퍼블릭 서브넷 + Public IP를
# 사용하므로 인터넷으로 직접 접근 가능 → 제거 (월 ~$28 절감)
###############################################################

# ── Gateway 엔드포인트: S3 (무료) ────────────────────────────
resource "aws_vpc_endpoint" "s3" {
  vpc_id            = var.vpc_id
  service_name      = "com.amazonaws.${var.aws_region}.s3"
  vpc_endpoint_type = "Gateway"
  route_table_ids   = [var.private_route_table_id, var.public_route_table_id]

  tags = { Name = "${var.project_name}-${var.env}-vpce-s3" }
}
