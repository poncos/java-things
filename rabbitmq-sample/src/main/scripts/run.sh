#!/bin/bash

java -Dlog4j.configuration=../config/log4j.xml -cp ../lib/*:../config com.ecollado.amqp.client.AmqpClientMain "$@"