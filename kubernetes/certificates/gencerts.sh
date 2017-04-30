#!/bin/sh

openssl req \
        -newkey rsa:2048 -nodes -keyout tls.key \
        -x509 -days 365 -out tls.crt
