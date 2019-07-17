FROM maven AS BUILD_IMAGE

ARG VERSION=1.0-SNAPSHOT
ARG THORNTAIL_VERSION=2.4.0.Final
ARG NAPO_DB_URL=jdbc:mariadb://napo-db:3306/napo
ARG NAPO_DB_SCHEMA=napo
ARG NAPO_DB_USER=root
ARG NAPO_DB_PASSWORD

# Preinstall some thorntail components for faster rebuilds

RUN mkdir -p /tmp/preload \
 && cd /tmp/preload \
 && mvn  -Dartifact=io.thorntail:ee:${THORNTAIL_VERSION} dependency:get \
 && mvn  -Dartifact=io.thorntail:undertow:${THORNTAIL_VERSION} dependency:get \
 && ls -1 ./ | xargs -I{} mvn -Dfile={} install:install-file

RUN mkdir -p /src
WORKDIR /src
COPY . /src
RUN mvn \
    -Ddb.napo.url=$NAPO_DB_URL \
    -Ddb.napo.username=$NAPO_DB_USER \
    -Ddb.napo.password=$NAPO_DB_PASSWORD \
    -Ddb.napo.schema=$NAPO_DB_SCHEMA \
    clean install


FROM openjdk:11

COPY --from=BUILD_IMAGE /src/comptoir-uber/target/comptoir-uber-thorntail.jar /

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8787" ]

CMD [ "-jar" , "/comptoir-uber-thorntail.jar" ]

