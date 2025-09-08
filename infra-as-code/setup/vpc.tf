module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "~> 6.0"

  name = "ecs-monorepo-gh-actions-vpc"
  cidr = "10.0.0.0/16"

  azs             = local.azs
  private_subnets = [for k, v in local.azs : cidrsubnet("10.0.0.0/16", 4, k)]
  public_subnets  = [for k, v in local.azs : cidrsubnet("10.0.0.0/16", 8, k + 48)]

  tags = {
    Environment = var.environment
    Project     = var.project_name
  }
}