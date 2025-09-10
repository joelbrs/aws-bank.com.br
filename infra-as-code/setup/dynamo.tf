module "dynamodb-table" {
  source  = "terraform-aws-modules/dynamodb-table/aws"
  version = "5.1.0"

  name         = "${var.project_name}-pipeline-lock-${var.environment}"
  hash_key     = "LockID"

  attributes = [
    {
      name = "LockID"
      type = "S"
    }
  ]

  tags = {
    Environment = var.environment
    Project     = var.project_name
  }
}