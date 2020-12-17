package com.ecollado.samples.rabbitmq.config;

import com.beust.jcommander.Parameter;
import com.ecollado.samples.rabbitmq.ExchangeType;
import com.ecollado.samples.rabbitmq.Operation;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by ecollado on 2/7/15.
 */
public class Config
{
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static final String EMPTY_STR = "";

    @Parameter(names = "-hostname", description = "AMQP broker hostname")
    private String hostname;

    @Parameter(names = "-port", description = "AMQP broker port")
    private int port;

    @Parameter(names = "-username", description = "AMQP broker port")
    private String userName;

    @Parameter(names = "-password", description = "AMQP broker port")
    private String password;

    @Parameter(names = "-exchange", description = "AMQP broker port")
    private String exchange;

    @Parameter(names = "-exchangetype", description = "AMQP broker port")
    private String exchangeType;

    @Parameter(names = "-queue", description = "AMQP broker port")
    private String queue;

    @Parameter(names = "-operation", description = "AMQP broker port")
    private String operation;

    @Parameter(names = "-create", description = "AMQP broker port")
    private Boolean createIfNecesary;

    @Parameter(names = "-routingkey", description = "AMQP broker port")
    private String routingKey;

    @Parameter(names = "-body", description = "AMQP broker port")
    private String body;

    @Parameter(names = "-typeid", description = "AMQP broker port")
    private String typeid;

    private PropertiesConfiguration properties;

    public String getHostname()
    {
        if (this.hostname == null || this.hostname.equals(EMPTY_STR))
        {
            hostname = this.getStringProperty("com.ecollado.amqp.client.remote.hostname");
        }

        return hostname;
    }

    public int getPort()
    {
        if (this.port == 0)
        {
            port = this.getIntProperty("com.ecollado.amqp.client.remote.port");
        }

        return port;
    }

    public String getUserName()
    {
        if (this.userName == null || this.userName.equals(EMPTY_STR))
        {
            userName = this.getStringProperty("com.ecollado.amqp.client.remote.user");
        }

        return userName;
    }

    public String getPassword()
    {
        if (this.password == null || this.password.equals(EMPTY_STR))
        {
            password = this.getStringProperty("com.ecollado.amqp.client.remote.password");
        }

        return password;
    }

    public String getExchange()
    {
        if (this.exchange == null || this.exchange.equals(EMPTY_STR))
        {
            exchange = this.getStringProperty("com.ecollado.amqp.client.remote.exchange.name");
        }

        return exchange;
    }

    public ExchangeType getExchangeType()
    {
        if (this.exchangeType == null || this.exchangeType.equals(EMPTY_STR))
        {
            exchangeType = this.getStringProperty("com.ecollado.amqp.client.remote.exchange.type");
        }

        return ExchangeType.valueOf(exchangeType);
    }

    public String getQueue()
    {
        if (this.queue == null || this.queue.equals(EMPTY_STR))
        {
            queue = this.getStringProperty("com.ecollado.amqp.client.remote.queue");
        }

        return queue;
    }

    public Operation getOperation()
    {
        if (this.operation == null || this.operation.equals(EMPTY_STR))
        {
            operation = this.getStringProperty("com.ecollado.amqp.client.operation");
        }

        return Operation.valueOf(operation);
    }

    public Boolean getCreateIfNecesary()
    {
        if (this.createIfNecesary == null)
        {
            createIfNecesary = this.getBooleanProperty("com.ecollado.amqp.client.declare.if.necesary");
        }

        return createIfNecesary;
    }

    public String getRoutingKey()
    {
        if (this.routingKey == null || this.routingKey.equals(EMPTY_STR))
        {
            routingKey = this.getStringProperty("com.ecollado.amqp.client.remote.routingkey");
        }

        return routingKey;
    }

    public String getTypeid()
    {
        if (this.typeid == null || this.typeid.equals(EMPTY_STR))
        {
            typeid = this.getStringProperty("com.ecollado.amqp.client.remote.typeid");
        }

        return typeid;
    }

    public String getBody()
    {
        //
        if (this.body == null || this.body.equals(EMPTY_STR))
        {
            body = this.getStringProperty("com.ecollado.amqp.client.message.body");
        }

        return body;
    }

    private String getStringProperty(String name)
    {

        this.readPropertiesConfiguration();

        return properties.getString(name);
    }

    private int getIntProperty(String name)
    {
        this.readPropertiesConfiguration();

        return properties.getInt(name);
    }

    private boolean getBooleanProperty(String name)
    {

        this.readPropertiesConfiguration();

        return properties.getBoolean(name,true);
    }

    private void readPropertiesConfiguration()
    {
        if (this.properties == null)
        {
            try
            {
                properties = new PropertiesConfiguration(
                        new File(this.getClass().getResource("/amqp-client.properties").toURI()));

            }
            catch (ConfigurationException e)
            {
                logger.error("Error reading configuration file", e);
            } catch (URISyntaxException e) {
                logger.error("Error reading configuration file", e);
            }
        }
    }
}
