package com.ecollado.samples.rabbitmq.listener;

import com.ecollado.samples.rabbitmq.handler.JsonMessageHandler;
import com.ecollado.samples.rabbitmq.handler.MessageHandler;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Created by ecollado on 2/7/15.
 */
public class AmqpClientMessageListener implements MessageListener
{
    public void onMessage(Message message)
    {
        MessageHandler handler = new JsonMessageHandler();

        handler.processMessage(message);
    }
}
