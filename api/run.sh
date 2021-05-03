#!/bin/bash
JVM_ARGS="$JVM_ARGS -server"

if [[ -n "$DEBUG_ENABLED" ]]; then
  JVM_ARGS="$JVM_ARGS -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=${DEBUG_SUSPEND:-n},address=*:9001"
fi

exec java $JVM_ARGS -cp .:./lib/* me.kolek.ecommerce.fis.api.Application
