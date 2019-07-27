#!/usr/bin/env bash

#docker-compose up -d napo-db
# --build-arg NAPO_DB_PASSWORD=password \
# --build-arg admin_password=password \
# --network comptoir-napo-db \

docker build \
 --tag comptoir-thorntail \
 -f comptoir-thorntail/Dockerfile \
 $@ \
 .
