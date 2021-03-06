FROM jboss/wildfly:17.0.1.Final AS APP_SERVER
ARG version=1.0-SNAPSHOT
ARG mariadb_version=2.4.2
ARG eclipselink_version=2.7.3
ARG admin_password

# Eclipselink module
ENV ECLIPSELINK_MODULE_PATH=/opt/jboss/wildfly/modules/system/layers/base/org/eclipse/persistence/main
RUN mkdir -p $ECLIPSELINK_MODULE_PATH
RUN curl -f https://search.maven.org/remotecontent?filepath=org/eclipse/persistence/eclipselink/${eclipselink_version}/eclipselink-${eclipselink_version}.jar \
    -o $ECLIPSELINK_MODULE_PATH/eclipselink.jar
COPY docker/eclipselink-module.xml $ECLIPSELINK_MODULE_PATH/module.xml

# Admin user & wildfly setup
RUN /opt/jboss/wildfly/bin/add-user.sh admin ${admin_password} --silent
COPY --from=BUILD_IMAGE /root/.m2/repository/org/mariadb/jdbc/mariadb-java-client/${mariadb_version}/mariadb-java-client-${mariadb_version}.jar \
    /tmp/mariadb-client.jar
COPY docker/setup-wildfly.cli /tmp/setup-wildfly.cli
COPY docker/setup-wildfly.sh /tmp/setup-wildfly.sh
RUN bash /tmp/setup-wildfly.sh

# Ðeployed archives
COPY comptoir-ear/target/comptoir-ear.ear  /opt/jboss/wildfly/standalone/deployments/

CMD ["/opt/jboss/wildfly/bin/standalone.sh","-c", "standalone-full.xml", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "--debug", "*:8787"]
