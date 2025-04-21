# CI/CD Pipeline Reference (Jenkins + Ansible + Terraform + Kubernetes)

This document outlines a robust, fully open source CI/CD pipeline for the e-Shop microservices project, leveraging Jenkins, Ansible, Terraform, and Kubernetes. It is intended as a blueprint for implementing automated build, test, infrastructure provisioning, deployment, and monitoring for all microservices.

---

## 1. Pipeline Overview

**Tools Used:**
- **Jenkins**: Pipeline orchestration, automation, and monitoring
- **Ansible**: Configuration management, app deployment
- **Terraform**: Infrastructure as Code (IaC) for provisioning cloud/on-prem resources
- **Kubernetes (K8s)**: Container orchestration and service management

**Stages:**
1. Code Checkout & Build
2. Unit/Integration Testing
3. Container Image Build & Push
4. Infrastructure Provisioning (Terraform)
5. Configuration & Secrets Management (Ansible)
6. Deploy to Kubernetes (via Jenkins/Ansible)
7. Post-Deployment Validation & Monitoring

---

## 2. Pipeline Stages in Detail

### 2.1. Code Checkout & Build
- Jenkins pulls source code from VCS (GitHub, GitLab, etc.).
- Each microservice is built using Maven (`mvn clean package`).

### 2.2. Unit/Integration Testing
- Jenkins runs unit and integration tests for each service.
- Test results are published in Jenkins for visibility.

### 2.3. Container Image Build & Push
- Jenkins builds Docker images (using Dockerfile or Jib plugin).
- Images are tagged with commit SHA or version.
- Images are pushed to an open source registry (Docker Hub, Harbor, etc.).

### 2.4. Infrastructure Provisioning (Terraform)
- Jenkins triggers Terraform scripts to provision/update infrastructure:
  - K8s clusters (on cloud or on-prem)
  - Networking, storage, and security resources
- Terraform state is stored securely (e.g., in S3, GCS, or local backend for PoC).

### 2.5. Configuration & Secrets Management (Ansible)
- Ansible playbooks configure servers, install dependencies, and manage secrets.
- Can be used to bootstrap K8s nodes, install monitoring agents, etc.

### 2.6. Deploy to Kubernetes
- Jenkins (or Ansible) applies K8s manifests (`kubectl apply -f ...`) for each service.
- Supports rolling updates, blue/green, or canary deployments.
- Helm charts can be used for templated deployments.

### 2.7. Post-Deployment Validation & Monitoring
- Jenkins runs smoke tests against deployed services.
- Monitoring (Prometheus, Grafana) and logging (ELK, Loki, etc.) are validated.
- Alerts and dashboards are checked for health.

---

## 3. Example Jenkins Pipeline (Declarative Syntax)

```groovy
pipeline {
  agent any
  environment {
    REGISTRY = 'docker.io/your-repo'
    KUBECONFIG = credentials('kubeconfig-cred')
  }
  stages {
    stage('Checkout') {
      steps {
        git 'https://github.com/your-org/e-Shop-microservices.git'
      }
    }
    stage('Build & Test') {
      steps {
        sh 'mvn clean package'
        junit '*/target/surefire-reports/*.xml'
      }
    }
    stage('Build & Push Image') {
      steps {
        sh 'docker build -t $REGISTRY/order-service:${BUILD_NUMBER} order-service/'
        sh 'docker push $REGISTRY/order-service:${BUILD_NUMBER}'
        // Repeat for other services
      }
    }
    stage('Terraform Apply') {
      steps {
        sh 'terraform init'
        sh 'terraform apply -auto-approve'
      }
    }
    stage('Ansible Configure') {
      steps {
        sh 'ansible-playbook -i inventory/hosts setup.yml'
      }
    }
    stage('Deploy to K8s') {
      steps {
        sh 'kubectl apply -f k8s/'
      }
    }
    stage('Smoke Test & Validate') {
      steps {
        sh './scripts/smoke-tests.sh'
      }
    }
  }
}
```

---

## 4. Directory Structure Recommendations

```
/jenkins/
  Jenkinsfile
/terraform/
  main.tf
  variables.tf
/ansible/
  inventory/hosts
  setup.yml
/k8s/
  order-service.yaml
  product-service.yaml
  ...
/scripts/
  smoke-tests.sh
```

---

## 5. Security & Best Practices
- Use Jenkins credentials for secrets (not hardcoded in pipeline).
- Store Terraform state securely.
- Use K8s secrets for sensitive data.
- Role-based access control (RBAC) in Jenkins, K8s, and cloud provider.
- Enable monitoring, logging, and alerting at every stage.

---

## 6. Open Source Stack
- Jenkins (https://jenkins.io)
- Ansible (https://ansible.com)
- Terraform (https://terraform.io)
- Kubernetes (https://kubernetes.io)
- Docker (https://docker.com)
- Prometheus, Grafana, Zipkin, ELK, Loki (monitoring/logging)

---

This pipeline is designed for modularity, scalability, and open source extensibility. Adapt paths, credentials, and tools to fit your environment and cloud provider.
