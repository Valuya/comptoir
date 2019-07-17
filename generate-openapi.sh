#!/usr/bin/env bash

MVN_HOME=/home/cghislai/apps/idea-IU-192.4488.21/plugins/maven/lib/maven3

# Currently, an app server must be running to generate the openapi yaml
# See https://github.com/eclipse/microprofile-open-api/issues/294
#docker-compose up -d comptoir

pushd comptoir-ws-api
  mvn clean
  mkdir target

  # Plugin should be able to fetch document from url directly (according to doc), but it fails
  # See https://github.com/OpenAPITools/openapi-generator/issues/2241
  curl -k -o target/comptoir-openapi-spec.yml https://comptoir.local:8443/openapi || exit 1

  mvn -Popenapi-codegen  package

  pushd target/generated-sources/openapi
#    sed -i 's#.*angular/http.*##' package.json
#    sed -i 's#.*typescript.*#    "typescript": "=3.4.5",#' package.json
    npm install
    npm run build
  popd
popd
