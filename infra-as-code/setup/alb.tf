module "alb" {
  source  = "terraform-aws-modules/alb/aws"
  version = "9.17.0"

  name = var.alb_internal_microservices_name

  load_balancer_type = "application"

  internal = true

  vpc_id  = module.vpc.vpc_id
  subnets = module.vpc.private_subnets

  security_group_ingress_rules = {
    all_http = {
      from_port   = 80
      to_port     = 80
      ip_protocol = "tcp"
      cidr_ipv4   = "0.0.0.0/0"
    }
  }

  security_group_egress_rules = {
    all = {
      ip_protocol = "-1"
      cidr_ipv4   = module.vpc.vpc_cidr_block
    }
  }

  listeners = {
    http = {
      port     = 80
      protocol = "HTTP"

      forward = {
        target_group_key = var.ms_authentication_project_name
      }

      rules = {
        (var.ms_authentication_project_name) = {
          priority = 100
          actions = [{
            type             = "forward"
            target_group_key = var.ms_authentication_project_name
          }]

          conditions = [{
            path_pattern = {
              values = ["/${var.ms_authentication_project_name}*"]
            }
          }]
        }
      }
    }
  }

  target_groups = {
    (var.ms_authentication_project_name) = {
      backend_protocol                  = "HTTP"
      backend_port                      = 8080
      target_type                       = "ip"
      deregistration_delay              = 5
      load_balancing_cross_zone_enabled = true

      health_check = {
        enabled             = true
        healthy_threshold   = 5
        interval            = 30
        matcher             = "200-302"
        path                = "/${var.ms_authentication_project_name}/health"
        port                = "traffic-port"
        protocol            = "HTTP"
        timeout             = 5
        unhealthy_threshold = 2
      }

      create_attachment = false
    }
  }

  tags = {
    Environment = var.environment
    Project     = var.project_name
  }
}