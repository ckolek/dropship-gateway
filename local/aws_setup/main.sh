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

function create_topic() {
  attempts=0
  retCode=-1
  while [[ $attempts -lt 10 ]] && [[ $retCode -ne 0 ]]; do
    aws --endpoint-url=${AWS_URL} sns create-topic --name "$1"
    retCode=$?
    ((attempts+=1))
    if [ $retCode -ne 0 ]; then
      echo failed with exit code $retCode - retry $attempts...
      sleep 1
    fi
  done
}

function create_topic_to_queue_subscription() {
  attempts=0
  retCode=-1
  while [[ $attempts -lt 10 ]] && [[ $retCode -ne 0 ]]; do
    aws --endpoint-url=${AWS_URL} sns subscribe \
        --topic-arn "arn:aws:sns:${AWS_DEFAULT_REGION}:${AWS_ACCOUNT_ID}:$1" \
        --protocol sqs \
        --notification-endpoint "arn:aws:sqs:${AWS_DEFAULT_REGION}:${AWS_ACCOUNT_ID}:$2" \
        --attributes "RawMessageDelivery=true"
    retCode=$?
    ((attempts+=1))
    if [ $retCode -ne 0 ]; then
      echo failed with exit code $retCode - retry $attempts...
      sleep 1
    fi
  done
}

function create_lambda() {
  attempts=0
  retCode=-1
  while [[ $attempts -lt 10 ]] && [[ $retCode -ne 0 ]]; do
    aws --endpoint-url=${AWS_URL} lambda create-function \
      --function-name $1 \
      --runtime $2 \
      --zip-file fileb:///aws/lambda/$3.zip \
      --handler $4 \
      --memory-size 128 \
      --role arn:aws:iam::${AWS_ACCOUNT_ID}:role/dummy \
      --environment Variables={$5} \
      --timeout 30
    retCode=$?
    ((attempts+=1))
    if [ $retCode -ne 0 ]; then
      echo failed with exit code $retCode - retry $attempts...
      sleep 1
    fi
  done
}

function create_topic_to_lambda_subscription() {
  attempts=0
  retCode=-1
  while [[ $attempts -lt 10 ]] && [[ $retCode -ne 0 ]]; do
    aws --endpoint-url=${AWS_URL} sns subscribe \
        --topic-arn "arn:aws:sns:${AWS_DEFAULT_REGION}:${AWS_ACCOUNT_ID}:$1" \
        --protocol lambda \
        --notification-endpoint "arn:aws:lambda:${AWS_DEFAULT_REGION}:${AWS_ACCOUNT_ID}:function:$2"
    retCode=$?
    ((attempts+=1))
    if [ $retCode -ne 0 ]; then
      echo failed with exit code $retCode - retry $attempts...
      sleep 1
    fi
  done
}

create_queue "order-actions"
create_queue "order-action-results"

create_topic "order-events"
create_queue "order-events"
create_topic_to_queue_subscription "order-events" "order-events"
create_lambda "order-event-handler" "java11" "event-handlers" \
              "me.kolek.ecommerce.dsgw.events.handler.OrderEventHandler" \
              "ELASTICSEARCH_HOSTS=${ELASTICSEARCH_HOSTS}"
create_topic_to_lambda_subscription "order-events" "order-event-handler"

create_topic "catalog-events"
create_queue "catalog-events"
create_topic_to_queue_subscription "catalog-events" "catalog-events"
create_lambda "catalog-event-handler" "java11" "event-handlers" \
              "me.kolek.ecommerce.dsgw.events.handler.CatalogEventHandler" \
              "ELASTICSEARCH_HOSTS=${ELASTICSEARCH_HOSTS}"
create_topic_to_lambda_subscription "catalog-events" "catalog-event-handler"
