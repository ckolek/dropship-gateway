FROM amazon/aws-cli

WORKDIR /aws

RUN yum install -y wget

ENV WAITFORIT_VERSION="v2.4.1"
RUN wget -q -O /usr/local/bin/waitforit https://github.com/maxcnunes/waitforit/releases/download/$WAITFORIT_VERSION/waitforit-linux_amd64 \
    && chmod +x /usr/local/bin/waitforit

ADD main.sh /aws
VOLUME /aws/lambda

ENTRYPOINT ["bash", "main.sh"]