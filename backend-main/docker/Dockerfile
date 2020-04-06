FROM openjdk:8-jre-alpine

ADD se3.jar /app.jar

ADD run.sh /run.sh

RUN chmod +x /run.sh

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

EXPOSE 9090

CMD /run.sh