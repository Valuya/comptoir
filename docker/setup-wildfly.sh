#!/bin/bash

/opt/jboss/wildfly/bin/standalone.sh -c standalone-full.xml -b 0.0.0.0 &

until `/opt/jboss/wildfly/bin/jboss-cli.sh -c "ls /deployment" &> /dev/null`; do
  sleep 1
done

/opt/jboss/wildfly/bin/jboss-cli.sh -c --file=/tmp/setup-wildfly.cli
/opt/jboss/wildfly/bin/jboss-cli.sh -c ":shutdown"
