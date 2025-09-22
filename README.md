# ARTH BANKING

## Prerequisites
- Docker
- Minikube (or any local k8s installation)
- Java 17+
- Gradle

## Setup Steps

### Step 1: Clone Repository
Clone the codebase from git

### Step 2: Start Docker
Start and verify Docker daemon is running:
```bash
docker ps
```

### Step 3: Start Minikube
```bash
minikube start
```

Configure shell to use Minikube's Docker daemon:
```bash
eval $(minikube docker-env)
```

### Step 4: Build Docker Image
Build the application jar locally first:
```bash
./gradlew bootJar
```

Build the Docker image:
```bash
docker build -t arth-banking:tag .
```

**Dockerfile:**
```dockerfile
FROM openjdk:17-jdk-slim
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Step 5: Set up Configuration and Deploy to Kubernetes

Create ConfigMap for application configuration:
```bash
kubectl create configmap app-config --from-literal=SPRING_PROFILES_ACTIVE=dev
```

Create Secret for database credentials:
```bash
kubectl create secret generic db-credentials \
  --from-literal=ARTH_DB_USERNAME=user \
  --from-literal=ARTH_DB_PASSWORD=password
```

Update the k8s/app-deployment.yml file with the `tag` specified while building the Docker image

Apply all Kubernetes YAML files:
```bash
kubectl apply -f k8s/
```

### Step 6: Verify Pod Creation
Check that all pods are running:
```bash
kubectl get pods
```

Optional: Launch Minikube dashboard for visual inspection:
```bash
minikube dashboard
```

### Step 7: Port Forwarding
Forward the service port to access the application:
```bash
kubectl port-forward svc/arth-banking 8080:80
```

### Step 8: Test APIs

**Get Account by ID:**
```bash
curl --location 'http://localhost:8080/accounts/1'
```

**Create Account:**
```bash
curl --location 'http://localhost:8080/accounts' \
--header 'Content-Type: application/json' \
--data '{
  "documentNumber": "99899889898"
}'
```

**Create Transaction:**
```bash
curl --location 'http://localhost:8080/transactions' \
--header 'Content-Type: application/json' \
--data '{
    "accountId": 1,
    "operationTypeId": 3,
    "amount": 3
}'
```