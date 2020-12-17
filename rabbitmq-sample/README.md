This project consist on an configurable and generic RabbitAMQP client application. This applicaiton can be used to publish and listen for messages in a rabbitMQ broker.

As technologies used by this application are: Spring AMQP for the messaging layer, JCommander for the configuration, and therefore this project can be used as example about how to use
SpringAMQP, more concrete, this applicaiton uses SpringAMQP directly from the source code without delegate on xml files or Annotations the configuration for the queues, exchanges, etc, and therefore
this application can be used as a good example to learn how to use SpringAMQP directly from source code.

# Environment and Build Instructions #

Install the [RabbitMQ](http://www.rabbitmq.com) broker. For example a generic package can be downloaded for linux, and to get it running on your box.

    $ tar xvzf rabbitmq-server-generic-unix-3.4.3.tar.gz
    $ cd rabbitmq_server-3.4.3/sbin
    $ ./rabbitmq-server
    
Once the RabbitMQ available, the application is packaged in a zip file using the assembly maven plugin. To generate the zip package, go to the project directory and execute.

    $ mvn clean package assembly:single

And then to execute the applicaiton.

    $ cd target
    $ unzip amqp-client-0.0.0.1-bin.zip
    $ cd amqp-client-0.0.0.1
    $ cd bin
    $ sh run.sh [parameters]
    
The parameters can be seen in the class "com.ecollado.amqp.client.config.Config".

The application can be configured to be executed as a server listening for messages coming in from RabbitMQ, or to be executed as a Client to publish messages into RabbitMQ broker.

## Listener

pending

## Publisher

pending
