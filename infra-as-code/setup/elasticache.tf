module "elasticache" {
  source  = "terraform-aws-modules/elasticache/aws"
  version = "1.7.0"

  cluster_id               = var.cache_cluster_id
  create_cluster           = true
  create_replication_group = false

  engine          = "memcached"
  engine_version  = "1.6.17"
  node_type       = "cache.t3.micro"
  num_cache_nodes = 2
  az_mode         = "cross-az"

  maintenance_window = "sun:05:00-sun:09:00"
  apply_immediately  = true

  vpc_id = module.vpc.vpc_id

  security_group_rules = {
    ingress_vpc = {
      from_port   = 11211
      to_port     = 11211
      protocol    = "tcp"
      description = "VPC traffic"
      cidr_ipv4   = module.vpc.vpc_cidr_block
    }
  }

  subnet_ids = module.vpc.private_subnets

  create_parameter_group = true
  parameter_group_family = "memcached1.6"
  parameters = [
    {
      name  = "idle_timeout"
      value = 60
    }
  ]

  tags = {
    Environment = var.environment
    Project     = var.project_name
  }
}