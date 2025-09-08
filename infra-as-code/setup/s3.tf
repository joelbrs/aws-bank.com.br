resource "aws_s3_bucket" "poc-ecs-monorepo-statefile-gh-actions-develop" {
  bucket = "${var.project_name}-pipeline-statefile-${var.environment}"

  tags = {
    Environment = var.environment
    Project     = var.project_name
  }
}