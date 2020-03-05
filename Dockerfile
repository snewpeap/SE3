FROM openjdk:8-jre-alpine

ADD ./target/se3.jar /app.jar

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=product"]