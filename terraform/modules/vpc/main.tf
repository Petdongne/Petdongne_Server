###############################################################
# modules/vpc/main.tf
###############################################################

# ── VPC ──────────────────────────────────────────────────────
resource "aws_vpc" "main" {
  cidr_block           = var.vpc_cidr
  enable_dns_hostnames = true   # 인터페이스 엔드포인트 DNS 해석 필수
  enable_dns_support   = true

  tags = { Name = "${var.project_name}-${var.env}-vpc" }
}

# ── 퍼블릭 서브넷 (ALB) ───────────────────────────────────────
resource "aws_subnet" "public" {
  count             = length(var.azs)
  vpc_id            = aws_vpc.main.id
  cidr_block        = var.public_subnet_cidrs[count.index]
  availability_zone = var.azs[count.index]

  map_public_ip_on_launch = true

  tags = { Name = "${var.project_name}-${var.env}-public-${count.index + 1}" }
}

# ── 프라이빗 서브넷 (ElastiCache / RDS) ──────────────────────
resource "aws_subnet" "private" {
  count             = length(var.azs)
  vpc_id            = aws_vpc.main.id
  cidr_block        = var.private_subnet_cidrs[count.index]
  availability_zone = var.azs[count.index]

  tags = { Name = "${var.project_name}-${var.env}-private-${count.index + 1}" }
}

# ── Internet Gateway (ALB용) ──────────────────────────────────
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id
  tags   = { Name = "${var.project_name}-${var.env}-igw" }
}

# ── 퍼블릭 라우팅 테이블 ──────────────────────────────────────
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }

  tags = { Name = "${var.project_name}-${var.env}-rt-public" }
}

resource "aws_route_table_association" "public" {
  count          = length(var.azs)
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

# ── 프라이빗 라우팅 테이블 (NAT 없음 — 엔드포인트로 대체) ─────
resource "aws_route_table" "private" {
  vpc_id = aws_vpc.main.id
  # 인터넷 경로 없음: 모든 외부 접근은 VPC 엔드포인트를 통해서만
  tags = { Name = "${var.project_name}-${var.env}-rt-private" }
}

resource "aws_route_table_association" "private" {
  count          = length(var.azs)
  subnet_id      = aws_subnet.private[count.index].id
  route_table_id = aws_route_table.private.id
}
