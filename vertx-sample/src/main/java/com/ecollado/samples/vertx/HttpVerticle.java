package com.ecollado.samples.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpVerticle.class);

    private HttpServer server;

    // Called when verticle is deployed
    @Override
    public void start(Future<Void> startFuture) {

        LOGGER.debug("HttpVerticle starting");

        server = vertx.createHttpServer().requestHandler(req -> {

            LOGGER.debug("Http handler");
            Handler httpHandler = new DefaultHttpHandler();
            httpHandler.handle(req);
        });

        // Now bind the server:
        server.listen(8080, res -> {
            if (res.succeeded()) {
                LOGGER.debug("HttpVerticle started");
                startFuture.complete();
            } else {
                startFuture.fail(res.cause());
            }
        });

    }

    // Optional - called when verticle is undeployed
    @Override
    public void stop(Future<Void> stopFuture) {
        LOGGER.debug("HttpVerticle stopping");
        server.close(res -> {
            if (res.succeeded()) {
                LOGGER.debug("HttpVerticle stopped");
                stopFuture.complete();
            } else {
                stopFuture.fail(res.cause());
            }
        });
    }
}
