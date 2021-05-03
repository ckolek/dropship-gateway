#!/bin/bash

./wait-for-it.sh "${HOST}:${PORT}" -t 30

flyway -url="jdbc:postgresql://${HOST}:${PORT}/${DB_NAME}" -user=${USERNAME} -password=${PASSWORD} ${OPERATION}