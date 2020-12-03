package com.ecollado.samples.vertx.eir.config;

import io.vertx.core.json.JsonObject;

public class IdentityCheckConfig {

    private final JsonObject config;

    public IdentityCheckConfig(JsonObject aConfig) {
        this.config = aConfig;
    }

    public DatabaseConfig getDatabaseConfig(String name) {

        JsonObject databasesCfg = this.config.getJsonObject("database");
        if (databasesCfg == null) {
            throw new IllegalArgumentException("No database configuration found.");
        }

        JsonObject dbCfg= databasesCfg.getJsonObject(name);
        if (dbCfg == null) {
            throw new IllegalArgumentException(String.format("database [%s] not configured", name));
        }

        int port = dbCfg.getInteger("port");
        String host = dbCfg.getString("host");
        int maxPoolSize = dbCfg.getInteger("max-pool-size");
        String username = dbCfg.getString("user");
        String password = dbCfg.getString("pwd");
        String dbname = dbCfg.getString("dbname");

        return new DatabaseConfig(dbname, port, host, maxPoolSize, username, password);
    }

    public HttpServerConfig getHttpServerConfig(String name) {

        JsonObject httpServersCfg = this.config.getJsonObject("http-server");
        if (httpServersCfg == null) {
            throw new IllegalArgumentException("No http servers configuration found.");
        }

        JsonObject httpServerConfig= httpServersCfg.getJsonObject(name);
        if (httpServerConfig == null) {
            throw new IllegalArgumentException(String.format("http-server [%s] not configured", name));
        }

        int port = httpServerConfig.getInteger("port");
        String bindAddress = httpServerConfig.getString("bind");

        return new HttpServerConfig(port, bindAddress);
    }
}
