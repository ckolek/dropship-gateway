#!/usr/bin/env bash

AWS_URL=http://${AWS_HOST}:${AWS_PORT}
AWS_DEFAULT_REGION=${AWS_DEFAULT_REGION:-us-east-1}
AWS_ACCOUNT_ID=${AWS_ACCOUNT_ID:-000000000000}

waitforit -host=${AWS_HOST} -port=${AWS_PORT} -timeout=60

function create_queue() {
  attempts=0
  retCode=-1
  while [[ $attempts -lt 10 ]] && [[ $retCode -ne 0 ]]; do
    aws --endpoint-url=${AWS_URL} sqs create-queue --queue-name "$1-deadletter"
    aws --endpoint-url=${AWS_URL} sqs create-queue --queue-name "$1" \
        --attributes "{\"RedrivePolicy\": \"{\\\"deadLetterTargetArn\\\":\\\"arn:aws:sqs:${AWS_DEFAULT_REGION}:${AWS_ACCOUNT_ID}:$1-deadletter\\\",\\\"maxReceiveCount\\\":\\\"3\\\"}\"}"
    retCode=$?
    ((attempts+=1))
    if [ $retCode -ne 0 ]; then
      echo failed with exit code $retCode - retry $attempts...
      sleep 1
    fi
  done
}

function create_fifo_queue() {
  attempts=0
  retCode=-1
  while [[ $attempts -lt 10 ]] && [[ $retCode -ne 0 ]]; do
    aws --endpoint-url=${AWS_URL} sqs create-queue --queue-name "$1-deadletter.fifo" \
        --attributes "{\"FifoQueue\" : \"true\"}"
    aws --endpoint-url=${AWS_URL} sqs create-queue --queue-name "$1.fifo" \
        --attributes "{\"FifoQueue\" : \"true\", \"ContentBasedDeduplication\":\"true\",\"RedrivePolicy\": \"{\\\"deadLetterTargetArn\\\":\\\"arn:aws:sqs:${AWS_DEFAULT_REGION}:${AWS_ACCOUNT_ID}:$1-deadletter.fifo\\\",\\\"maxReceiveCount\\\":\\\"3\\\"}\"}"
    retCode=$?
    ((attempts+=1))
    if [ $retCode -ne 0 ]; then
      echo failed with exit code $retCode - retry $attempts...
      sleep 1
    fi
  done
}

create_queue "order-actions"
