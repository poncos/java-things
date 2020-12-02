package com.ecollado.samples.vertx.eir.service;

import com.ecollado.samples.vertx.eir.config.HttpServerConfig;
import com.ecollado.samples.vertx.eir.config.IdentityCheckConfig;
import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdentityCheckHttpVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityCheckHttpVerticle.class);

    @Override
    public void start() throws Exception {

        IdentityCheckConfig config      = new IdentityCheckConfig(this.config());
        HttpServerConfig    httpConfig  = config.getHttpServerConfig("eir");

        vertx.createHttpServer()
                .requestHandler(new DefaultHttpRequestHandler(vertx))
                .listen(httpConfig.getPort(), httpConfig.getBind(), res -> {
                    if (res.succeeded()) {
                        LOGGER.info("Server is now listening!");
                    } else {
                        LOGGER.error("problems starting HTTP Server [{}]", res.cause());
                        throw new RuntimeException("problems starting HTTP Server", res.cause());
                    }
        });
    }

}
