FROM nginx:1.11.6

COPY nginx.conf /etc/nginx/
COPY client.conf /etc/nginx/conf.d/

COPY app /opt/client

RUN chmod -R ugo+rX /opt/client && \
    rm /etc/nginx/conf.d/default.conf