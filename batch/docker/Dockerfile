FROM openjdk:8-alpine-netcat

ADD se3.jar /app.jar

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

EXPOSE 9910
EXPOSE 9911
EXPOSE 9912

ENTRYPOINT ["java", "-XX:MinHeapFreeRatio=16", "-Dspring.profiles.active=daoproduct,product", "-jar", "/app.jar"]