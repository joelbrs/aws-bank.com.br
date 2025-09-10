module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "~> 6.0"

  name = "ecs-monorepo-gh-actions-vpc"
  cidr = "10.0.0.0/16"

  azs             = local.azs
  private_subnets = [for k, v in local.azs : cidrsubnet("10.0.0.0/16", 4, k)]
  public_subnets  = [for k, v in local.azs : cidrsubnet("10.0.0.0/16", 8, k + 48)]

  enable_nat_gateway = true
  single_nat_gateway = false
  one_nat_gateway_per_az = true

  tags = {
    Environment = var.environment
    Project     = var.project_name
  }
}