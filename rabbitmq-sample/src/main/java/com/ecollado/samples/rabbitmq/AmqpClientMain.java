package com.ecollado.samples.rabbitmq;

import com.beust.jcommander.JCommander;
import com.ecollado.samples.rabbitmq.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by ecollado on 06/02/15.
 */
public class AmqpClientMain
{

    private static final Logger logger = LoggerFactory.getLogger(AmqpClientMain.class);

    private final Config config;

    public AmqpClientMain(Config config)
    {
        this.config = config;
    }

    public void execute()
    {

        logger.info("Launching AmqpClient [{}]",config.getOperation());

        RabbitmqOperations rabbitmqOperations =
                new RabbitmqOperations(config.getHostname(), config.getUserName(), config.getPassword() );

        switch (config.getOperation())
        {
            case listen:
                rabbitmqOperations.listen(
                        config.getQueue(), config.getExchange(), config.getExchangeType(),
                        config.getRoutingKey(),config.getCreateIfNecesary());

                logger.info("Listener waiting for message from queue [{}]  and routingKey: [{}]" ,
                        config.getQueue(), config.getRoutingKey());
                for(;;)
                {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {}
                }

            case publish:
                rabbitmqOperations.publish(
                        config.getBody(),
                        config.getExchange(),
                        config.getExchangeType(),
                        config.getRoutingKey(),
                        config.getTypeid(),
                        config.getCreateIfNecesary()
                );
                break;
        }
    }

    public static void main(String args[])
    {
        Config config = new Config();
        new JCommander(config, args);

        AmqpClientMain amqpClient = new AmqpClientMain(config);
        amqpClient.execute();
    }
}
