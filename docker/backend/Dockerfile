FROM igeolise/openjdk:8

ADD backend.tgz /opt

RUN mv /opt/* /opt/backend

VOLUME ["/opt/backend/logs"]

CMD cd /opt/backend && bin/backend