FROM python:alpine

ARG CLI_VERSION=1.16.86

RUN apk -uv add --no-cache groff jq less bash && \
    pip install --no-cache-dir awscli==$CLI_VERSION

WORKDIR /aws

ENV WAITFORIT_VERSION="v2.4.1"
RUN wget -q -O /usr/local/bin/waitforit https://github.com/maxcnunes/waitforit/releases/download/$WAITFORIT_VERSION/waitforit-linux_amd64 \
    && chmod +x /usr/local/bin/waitforit

ADD main.sh /aws

ENTRYPOINT ["bash", "main.sh"]