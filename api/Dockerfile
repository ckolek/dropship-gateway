FROM openjdk:11-jdk-slim
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
EXPOSE 9001
COPY ${DEPENDENCY}/run.sh /app
RUN apt-get -y -qq update
WORKDIR /app
ENTRYPOINT ["bash", "run.sh"]