#!/usr/bin/env bash

echo "[-->] Start Traefik using Compose as usual"
docker-compose -f docker-compose-traefik-only.yml up -d

echo "[-->] Startup weatherbackend without Compose - but connect to Traefik"
docker build ./weatherbackend --tag weatherbackend
docker run \
  --rm \
  -d \
  -p 8095 \
  --label="traefik.enable=true" \
  --label="traefik.http.routers.whoami.entrypoints=web" \
  --label="traefik.http.routers.weatherbackend.rule=Host(\`weatherbackend.server.test\`)" \
  --network="traefik-cache-nginx-spring-boot_traefiknet" \
  --name weatherbackend \
  weatherbackend

echo "[-->] Startup weatherclient without Compose - but connect to Traefik"
docker build ./weatherclient --tag weatherclient
docker run \
  --rm \
  -d \
  -p 8085:8085 \
  --network="traefik-cache-nginx-spring-boot_traefiknet" \
  --name weatherclient \
  weatherclient
