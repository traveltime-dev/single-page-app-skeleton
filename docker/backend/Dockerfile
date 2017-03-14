FROM igeolise/openjdk:8

ADD backend.tgz /opt

RUN mv /opt/* /opt/spa-backend

VOLUME ["/opt/spa-backend/logs"]
VOLUME ["/opt/spa-backend/filebeat"]

CMD cd /opt/spa-backend && bin/backend