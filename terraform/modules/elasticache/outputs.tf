output "endpoint" {
  value = aws_elasticache_serverless_cache.valkey.endpoint[0].address
}

output "port" {
  value = aws_elasticache_serverless_cache.valkey.endpoint[0].port
}
