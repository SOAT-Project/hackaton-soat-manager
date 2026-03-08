################################################################################
# Namespace
################################################################################

resource "kubernetes_manifest" "namespace" {
  manifest = yamldecode(file("${path.module}/../kubernetes/namespace.yaml"))
}

################################################################################
# ServiceAccount com IRSA
################################################################################

resource "kubernetes_service_account" "service_account" {
  metadata {
    name      = var.service_account_name  # hackaton-soat-manager-sa
    namespace = var.kubernetes_namespace  # hackaton-soat-manager

    annotations = {
      "eks.amazonaws.com/role-arn" = data.aws_iam_role.manager_irsa.arn
    }
  }

  depends_on = [kubernetes_manifest.namespace]
}

################################################################################
# ConfigMap
################################################################################

resource "kubernetes_config_map" "app_config" {
  metadata {
    name      = "app-config"
    namespace = var.kubernetes_namespace
  }

  data = {
    APPLICATION_PORT = var.application_port
    AWS_REGION       = var.aws_region

    # S3
    AWS_BUCKET = data.aws_s3_bucket.web_bucket.bucket

    # Dynamo
    AWS_DYNAMO_TABLE = data.aws_dynamodb_table.processing.name

    # SQS - PROCESS
    AWS_PROCESS_QUEUE     = data.aws_sqs_queue.process.name
    AWS_PROCESS_QUEUE_URL = data.aws_sqs_queue.process.url

    # SQS - PROCESSED
    AWS_PROCESSED_QUEUE     = data.aws_sqs_queue.processed.name
    AWS_PROCESSED_QUEUE_URL = data.aws_sqs_queue.processed.url

    # SQS - NOTIFY
    AWS_NOTIFY_QUEUE     = data.aws_sqs_queue.notify.name
    AWS_NOTIFY_QUEUE_URL = data.aws_sqs_queue.notify.url
  }

  depends_on = [kubernetes_manifest.namespace]
}

################################################################################
# Deployment
################################################################################

resource "kubernetes_manifest" "deployment" {
  manifest = yamldecode(file("${path.module}/../kubernetes/deployment.yaml"))

  depends_on = [
    kubernetes_manifest.namespace,
    kubernetes_service_account.service_account,
    kubernetes_config_map.app_config
  ]
}

################################################################################
# Service (ClusterIP)
################################################################################

resource "kubernetes_manifest" "service" {
  manifest = yamldecode(file("${path.module}/../kubernetes/service.yaml"))

  depends_on = [
    kubernetes_manifest.namespace,
    kubernetes_manifest.deployment
  ]
}

################################################################################
# HPA (Horizontal Pod Autoscaler)
################################################################################

resource "kubernetes_manifest" "hpa" {
  manifest = yamldecode(file("${path.module}/../kubernetes/hpa.yaml"))

  depends_on = [
    kubernetes_manifest.namespace,
    kubernetes_manifest.deployment
  ]
}

################################################################################
# HTTPRoute (para Envoy/Ingress)
################################################################################

resource "kubernetes_manifest" "httproute" {
  manifest = yamldecode(file("${path.module}/../kubernetes/httproute.yaml"))

  depends_on = [
    kubernetes_manifest.namespace,
    kubernetes_manifest.service
  ]
}