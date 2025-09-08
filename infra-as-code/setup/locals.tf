locals {
  azs  = ["${var.aws_region}a", "${var.aws_region}b", "${var.aws_region}c"]

  ms_authentication_image_tag = "${var.environment}-${var.ms_authentication_project_name}-${var.apps_version_default}"
  ms_authentication_container_name = "${var.environment}-${var.ms_authentication_project_name}"
}