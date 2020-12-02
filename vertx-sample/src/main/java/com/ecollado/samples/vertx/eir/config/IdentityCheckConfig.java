package com.ecollado.samples.vertx.eir.service;

import io.vertx.core.json.JsonObject;

public class IdentityCheckConfig {

    private final JsonObject config;

    public IdentityCheckConfig(JsonObject aConfig) {
        this.config = aConfig;
    }

    public void getDatabaseConfig(String name) {
        this.config.getJsonObject()
    }

    class DatabaseConfig {
        private int port;
        private String host;
        private int maxPoolSize;
        private String username;
        private String password;


    }

}
