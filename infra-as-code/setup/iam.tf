module "iam_oidc_provider" {
  source    = "terraform-aws-modules/iam/aws//modules/iam-oidc-provider"

  url = "https://token.actions.githubusercontent.com"

  tags = {
    Environment = var.environment
    Project     = var.project_name
  }
}

module "iam_role_github_oidc" {
  source    = "terraform-aws-modules/iam/aws//modules/iam-role"

  name = "gh-actions-role"

  enable_github_oidc = true

  oidc_wildcard_subjects = ["repo:joelbrs/aws-bank.com.br:*"]

  policies = {
    S3FullAccess  = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
    DynamoDBFullAccess = "arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess"    
    ECSFullAccess = "arn:aws:iam::aws:policy/AmazonECS_FullAccess"
    ECRPowerUser = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryPowerUser"
  }

  tags = {
    Environment = var.environment
    Project     = var.project_name
  }
}

resource "aws_iam_role" "ecs_task_execution_role" {
  name = "EcsTaskExecutionRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = {
        Service = "ecs-tasks.amazonaws.com"
      }
      Action = "sts:AssumeRole"
    }]
  })

  tags = {
    Environment = var.environment
    Project     = var.project_name
  }
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}