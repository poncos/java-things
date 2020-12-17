package com.ecollado.samples.rabbitmq;

/**
 * Created by ecollado on 06/02/15.
 */

import com.ecollado.samples.rabbitmq.listener.AmqpClientMessageListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.DefaultClassMapper;


public class RabbitmqOperations
{
    private static final String MESSAGES_CONTENT_TYPE = MessageProperties.CONTENT_TYPE_JSON;

    private String hostname;

    private String user;

    private String password;

    public RabbitmqOperations(String hostname, String user,String password)
    {
        this.hostname   = hostname;
        this.user       = user;
        this.password   = password;
    }

    public void publish(
            String body,
            String exchangeName,
            ExchangeType exchangeType,
            String routingKey,
            String typeId,
            boolean createIfNecessary)
    {
        ConnectionFactory   connectionFactory   = connectionFactory();
        RabbitTemplate      rabbitTemplate      = rabbitTemplate(connectionFactory);
        AmqpAdmin           amqpAdmin           = amqpAdmin(connectionFactory);

        Exchange    exchange    = (exchangeType == ExchangeType.direct) ?
                directExchange(exchangeName):topicExchange(exchangeName);

        if(createIfNecessary)
        {
            declareElements(amqpAdmin, null, exchange);
        }

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MESSAGES_CONTENT_TYPE);

        Message message = new Message(body.getBytes(),messageProperties);
        messageProperties.setHeader(DefaultClassMapper.DEFAULT_CLASSID_FIELD_NAME,typeId);

        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, message);
    }

    public void listen(
            String queueName,
            String exchangeName,
            ExchangeType exchangeType,
            String routingKey,
            boolean createIfNecessary
    )
    {
        ConnectionFactory   connectionFactory   = connectionFactory();
        AmqpAdmin           amqpAdmin           = amqpAdmin(connectionFactory);

        Queue       queue       = queue(queueName);
        Exchange    exchange    = (exchangeType == ExchangeType.direct) ?
                directExchange(exchangeName):topicExchange(exchangeName);

        Binding binding = bindingWithExchange(
                queue, exchange, routingKey);

        if(createIfNecessary)
        {
            declareElements(amqpAdmin, queue, exchange);
        }

        amqpAdmin.declareBinding(binding);

        SimpleMessageListenerContainer listenerContainer =
                new SimpleMessageListenerContainer();

        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setQueues(queue);
        listenerContainer.setMessageListener(new AmqpClientMessageListener());


        listenerContainer.start();
    }

    private ConnectionFactory connectionFactory()
    {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostname);
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(password);

        return connectionFactory;
    }

    private AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory)
    {
        AmqpAdmin amqpAdmin = new RabbitAdmin(connectionFactory);

        return  amqpAdmin;
    }

    private RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory)
    {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        return template;
    }

    private Binding bindingWithExchange(
            Queue queue,
            Exchange exchange,
            String routingKey)
    {
        Binding binding;

        if (exchange instanceof DirectExchange)
        {
            binding = BindingBuilder.bind(queue).to((DirectExchange)exchange).with(routingKey);
        }
        else
        {
            binding = BindingBuilder.bind(queue).to((TopicExchange)exchange).with(routingKey);
        }
        return binding;
    }

    private Queue queue(String name)
    {
        Queue queue = new Queue(name);

        return queue;
    }

    private DirectExchange directExchange(String name)
    {
        DirectExchange exchange = new DirectExchange(name);

        return exchange;
    }

    private TopicExchange topicExchange(String name)
    {
        TopicExchange exchange = new TopicExchange(name);

        return exchange;
    }

    private void declareElements(AmqpAdmin amqpAdmin,
                                  Queue queue,
                                  Exchange exchange)
    {
        if (exchange != null)
            amqpAdmin.declareExchange(exchange);
        if (queue != null)
            amqpAdmin.declareQueue(queue);
    }
}
