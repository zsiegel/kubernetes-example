# Kubernetes Example

We will deploy the following
- A Java 8 webapp that uses the Vert.x framework
- A container that runs MySQL with a persistent volume
- A kubernetes cluster that uses secrets, volumes, deployments, and services

## Walkthrough

Build the fat jar that will be copied into a docker container
```
gradle stage
```

Build the docker container
```
docker build -t zcorp-api:v1 zcorp-api/
```

Launch [Minikube](https://github.com/kubernetes/minikube) (a local kubernetes cluster)
```
minikube start --vm-driver=xhyve
```

Allow local Docker access
```
eval $(minikube docker-env)
```

Add secrets to cluster
```shell
kubectl apply -f kubernetes/secrets/
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
If the service is running it will return
```json
{
  "env": "k8-zcorp",
  "now": "the current timestamp"
}
```

Verify the database is connected by visiting `/healthz`

If connected it will return
```json
{
  "database": 1
}
```

---

`TODO` Figure out a way to make minikube serve ingress on a different IP than the cluster IP
so that the self signed certificate is presented instead of minikube temp cert

Verify SSL certificate setup using ingress

You must add ingress support to minikube
```
minikube addons enable ingress
```

Deploy ingress
```
kubectl apply -f kubernetes/ingress/
```