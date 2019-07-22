FROM openjdk:11

COPY target/comptoir-thorntail-thorntail.jar /comptoir-thorntail.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8787" ]

CMD [ "-jar" , "/comptoir-thorntail.jar" ]

