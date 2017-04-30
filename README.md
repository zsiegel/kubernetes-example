# Kubernetes Example

- Java 1.8
- MySQL
- Vert.x


Build docker container
```
docker build -t zcorp-api:v6 zcorp-api/
```
Launch Minikube
```
minikube start --vm-driver=xhyve
```

Allow local Docker access
```
eval $(minikube docker-env)
```

Enable ingress
```
minikube addons enable ingress
```

Add secrets to cluster
```shell
kubectl apply secret -f kubernetes/secrets/
```

Add storage to cluster
```shell
kubectl apply -f kubernetes/volumes/
```

Deploy the service
```shell
kubectl apply -f kubernetes/services/
```

Deploy the pods
```shell
kubectl apply -f kubernetes/deployments/
```

Verify the service is working
```
minikube service zcorp
```