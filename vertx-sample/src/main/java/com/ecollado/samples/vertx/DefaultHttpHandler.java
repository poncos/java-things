package com.ecollado.samples.vertx;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultHttpHandler implements HttpHandler, Handler<HttpServerRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpHandler.class);

    @Override
    public void handle(HttpServerRequest request) {
        LOGGER.debug("handle");

        switch (request.method()) {
            case POST:
                doPost();
                break;
            case GET:
                doGet();
                break;
            case PUT:
                doPut();
                break;
            case DELETE:
                doDelete();
                break;
            case PATCH:
                doPatch();
                break;
        }
        request.response()
                .putHeader("content-type", "text/plain")
                .end("Hello from Vert.x!");
    }

    @Override
    public void doPost() {
        LOGGER.debug("doPost");
    }

    @Override
    public void doGet() {
        LOGGER.debug("doGet");
    }

    @Override
    public void doPut() {
        LOGGER.debug("doPut");
    }

    @Override
    public void doDelete() {
        LOGGER.debug("doPut");
    }

    @Override
    public void doPatch() {
        LOGGER.debug("doPut");
    }
}
