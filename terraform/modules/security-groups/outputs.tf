output "sg_alb_id" {
  value = aws_security_group.alb.id
}

output "sg_ecs_id" {
  value = aws_security_group.ecs.id
}

output "sg_rds_id" {
  value = aws_security_group.rds.id
}

output "sg_elasticache_id" {
  value = aws_security_group.elasticache.id
}
