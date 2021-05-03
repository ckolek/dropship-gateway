FROM flyway/flyway:7.8.1-alpine

ENV HOST "localhost"
ENV PORT "5432"
ENV DB_NAME "dsgw"
ENV USERNAME "dsgw"
ENV PASSWORD "dsgw"
ENV OPERATION "migrate"

RUN wget https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh
RUN chmod +x wait-for-it.sh

ADD entrypoint.sh .
ADD schema/*.sql /flyway/sql/

ENTRYPOINT ["./entrypoint.sh"]