FROM nginx:1.11.6

COPY nginx.conf /etc/nginx/
COPY spa.conf /etc/nginx/conf.d/

COPY app /opt/spa-client

RUN chmod -R ugo+rX /opt/spa-client && \
    rm /etc/nginx/conf.d/default.conf