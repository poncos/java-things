package com.ecollado.samples.vertx.eir.service;

import com.ecollado.samples.vertx.eir.exception.ValidationException;
import com.ecollado.samples.vertx.eir.model.IdentityCheckRequest;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DefaultHttpRequestHandler implements Handler<HttpServerRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpRequestHandler.class);

    private final Vertx vertx;

    public DefaultHttpRequestHandler(Vertx aVertx) {
        this.vertx = aVertx;
    }

    @Override
    public void handle(HttpServerRequest request) {
        request.bodyHandler(
            totalBuffer -> {
                LOGGER.debug("Full body received, length = {}", totalBuffer.length());

                try {
                    DefaultHttpRequestHandler.this.validateRequest(request);

                    switch (request.method()) {
                        case POST:
                            doPost(request, totalBuffer);
                            break;
                        case GET:
                            doGet(request);
                            break;
                        case PUT:
                            doPut(request);
                            break;
                        case DELETE:
                            doDelete(request);
                            break;
                        case PATCH:
                            doPatch(request);
                            break;
                    }
                } catch (ValidationException e) {
                    LOGGER.error("Error validating request.", e);
                    request.response()
                            .setStatusCode(400)
                            .putHeader("content-type", "application/json")
                            .end(e.getMessage());
                } catch (RuntimeException | IOException e) {
                    LOGGER.error("Error processing request.", e);
                    request.response()
                            .setStatusCode(500)
                            .putHeader("content-type", "application/json")
                            .end("{ \"error-message\": \""+e.getMessage()+"\"}");
                }
            }
        );
    }

    private void validateRequest(HttpServerRequest request) throws ValidationException {
        String contentType      = request.getHeader("Content-Type");

        StringBuilder errorInfo = new StringBuilder();
        boolean errors = false;

        if (contentType == null || !contentType.equals("application/json")) {
            errorInfo.append("{ \"error-message\": \"Content-Type must be application/json\"}");
            errors = true;
        }

        if (errors) {
            throw new ValidationException(errorInfo.toString());
        }
    }

    public void doPost(HttpServerRequest request, Buffer body) throws IOException {
        LOGGER.debug("doPost");
        EventBus eb = vertx.eventBus();

        byte[] bytes = body.getBytes();
        IdentityCheckRequest identityCheckRequest = IdentityCheckRequest.fromBytes(bytes);

        eb.request("eir.identity-check-request", identityCheckRequest.toJsonStr(), reply -> {
            if (reply.succeeded()) {
                request.response()
                        .putHeader("content-type", "application/json")
                        .end("{ \"result\": \""+ reply.result().body() + "\"}");
            } else {
                request.response()
                        .setStatusCode(500)
                        .putHeader("content-type", "application/json")
                        .end("{ \"error-message\": \""+ reply.cause() + "\"}");
            }
        });
    }

    public void doGet(HttpServerRequest request) {
        throw new RuntimeException("not implemented");
    }

    public void doPut(HttpServerRequest request) {
        throw new RuntimeException("not implemented");
    }

    public void doDelete(HttpServerRequest request) {
        throw new RuntimeException("not implemented");
    }

    public void doPatch(HttpServerRequest request) {
        throw new RuntimeException("not implemented");
    }
}
