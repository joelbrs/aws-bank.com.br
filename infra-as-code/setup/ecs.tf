resource "aws_ecs_task_definition" "ms_authentication_task_definition" {
  family                   = "${var.environment}-${var.ms_authentication_project_name}"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 1024
  memory                   = 1024 * 3
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name      = local.ms_authentication_container_name
      image     = local.ms_authentication_image_tag
      cpu       = 10
      memory    = 512
      essential = true
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = "/ecs/${var.environment}-${var.ms_authentication_project_name}"
          awslogs-region        = var.aws_region
          awslogs-stream-prefix = "ecs"
        }
      }
    }
  ])

  runtime_platform {
    operating_system_family = "LINUX"
    cpu_architecture       = "X86_64"
  }
}

resource "aws_ecs_cluster" "aws_ecs_cluster" {
  name = var.project_name

  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}

resource "aws_ecs_service" "ms_authentication_ecs_service" {
  name            = "${var.environment}-${var.ms_authentication_project_name}"
  cluster         = aws_ecs_cluster.aws_ecs_cluster.id
  task_definition  = aws_ecs_task_definition.ms_authentication_task_definition.arn
  desired_count   = 1
  launch_type     = "FARGATE"
  
  network_configuration {
    subnets          = module.vpc.private_subnets
    security_groups  = [module.web_server_sg.security_group_id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = module.alb.target_groups[var.ms_authentication_project_name].arn
    container_name   = local.ms_authentication_container_name
    container_port   = 8080
  }
}