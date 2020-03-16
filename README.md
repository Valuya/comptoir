comptoir
========

# Prerequistes
## settings.xml

```

<profile>
    <id>comptoir-napo</id>
    <properties>
        <db.napo.url>jdbc:mariadb://localhost:22306/napo</db.napo.url>
        <db.napo.username>root</db.napo.username>
        <db.napo.password>NAPO DB PASSWORD</db.napo.password>
        <db.napo.schema>napo</db.napo.schema>
    </properties>
</profile>
<profile>
    <id>comptoir-thorntail</id>
    <properties>
            <comptoir.datasource.connection.password>DB PASSWORD</comptoir.datasource.connection.password>
            <comptoir.https.keytore.path>/certs/comptoir.local.p12</comptoir.https.keytore.path>
            <comptoir.https.keytore.password>password</comptoir.https.keytore.password>
            <comptoir.https.key.alias>comptoir.local</comptoir.https.key.alias>
            <comptoir.https.key.password>password</comptoir.https.key.password>
    </properties>
</profile>

```

## workdir ./secrets
secret files mounted in db containers

- db-root-password
- napo-db-root-password

## Initial dumps
- ../res/ relative to workdir (see compose file)

# Running

- docker-compose up -d db napo-db (wait for dump to import)
- mvn clean install
- docker-compose up --build back
