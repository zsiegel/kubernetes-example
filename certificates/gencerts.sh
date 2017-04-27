#!/bin/sh

openssl req \
        -newkey rsa:2048 -nodes -keyout tls.key \
        -x509 -days 365 -out tls.crt

kubectl create secret generic zcorp-https \
        --from-file=../certificates/tls.crt \
        --from-file=../certificates/tls.key