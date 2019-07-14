#!/usr/bin/env bash

docker-compose up -d napo-db

docker build \
 --build-arg NAPO_DB_PASSWORD=password \
 --build-arg admin_password=password \
 --network comptoir-napo-db \
 --tag comptoir-back \
 -f docker/Dockerfile \
 $@ \
 .
