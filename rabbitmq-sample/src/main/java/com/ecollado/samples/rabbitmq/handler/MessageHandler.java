package com.ecollado.samples.rabbitmq.handler;

import org.springframework.amqp.core.Message;

/**
 * Created by ecollado on 2/7/15.
 */
public interface MessageHandler
{
    void processMessage(Message message);
}
