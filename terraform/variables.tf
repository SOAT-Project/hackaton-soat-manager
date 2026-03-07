variable "eks_cluster_name" {
  type = string
}

variable "irsa_role_name" {
  type = string
}

variable "dynamodb_table_name" {
  type = string
}

variable "s3_bucket_name" {
  type = string
}

variable "sqs_notify_name" {
  type = string
}

variable "sqs_process_name" {
  type = string
}

variable "sqs_processed_name" {
  type = string
}

variable "kubernetes_namespace" {
  type    = string
  default = "hackaton-soat-manager"
}

variable "service_account_name" {
  type    = string
  default = "hackaton-soat-manager-sa"
}

variable "application_port" {
  type    = string
  default = "8080"
}

variable "aws_region" {
  type = string
}

variable "environment" {
  type = string
}
