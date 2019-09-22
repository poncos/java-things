package com.ecollado.samples.vertx;


public interface HttpHandler {

    void doPost();

    void doGet();

    void doPut();

    void doPatch();

    void doDelete();
}
