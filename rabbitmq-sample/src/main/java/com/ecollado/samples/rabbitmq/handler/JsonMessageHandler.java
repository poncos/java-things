package com.ecollado.samples.rabbitmq.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.DefaultClassMapper;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by ecollado on 2/7/15.
 */
public class JsonMessageHandler implements MessageHandler
{
    private static final Logger logger = LoggerFactory.getLogger(JsonMessageHandler.class);

    public void processMessage(Message message)
    {
        logger.info("Message received: " + message);

        if (message == null)
        {
            logger.error("Message null");
            return;
        }

        MessageProperties messageProperties = message.getMessageProperties();

        if ( !messageProperties.getContentType().equals(MessageProperties.CONTENT_TYPE_JSON))
        {
            logger.warn("Message content-type is different than " + MessageProperties.CONTENT_TYPE_JSON);
        }

        byte[] body = message.getBody();
        String encoding = messageProperties.getContentEncoding();
        Map<String,Object> headers = messageProperties.getHeaders();
        String typeId = "UNKNOWN_TYPE";
        String jsonMessage = null;

        if(headers.containsKey(DefaultClassMapper.DEFAULT_CLASSID_FIELD_NAME))
        {
            typeId = headers.get(DefaultClassMapper.DEFAULT_CLASSID_FIELD_NAME).toString();
        }

        if (encoding != null && !encoding.equals(""))
        {
            try
            {
                jsonMessage = new String(body, encoding);
            }
            catch (UnsupportedEncodingException e)
            {
                logger.info("Not possible decode the message using encoding from message: " + encoding);
            }
        }

        if (jsonMessage == null)
        {
            jsonMessage = new String(body);
        }

        logger.info("MESSAGE: (" + typeId + "): "+ jsonMessage);
    }
}
