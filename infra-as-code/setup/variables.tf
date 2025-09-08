variable "environment" {
    description = "The deployment environment (e.g., development, staging, production)"
    type        = string
    default     = "development"
}

variable "project_name" {
    description = "The name of the project"
    type        = string
    default     = "aws-bank"
}

variable "aws_region" {
    description = "The AWS region to deploy resources in"
    type        = string
    default     = "us-east-1"
}

variable "apps_version_default" {
    description = "The version of the applications"
    type        = string
    default     = "0.1.0"
}

variable "alb_internal_microservices_name" {
    description = "The internal ALB microservices name"
    type        = string
    default     = "alb-internal-microservices"
}

variable "ms_authentication_project_name" {
    description = "The ms authentication project name"
    type        = string
    default     = "ms-authentication"
}