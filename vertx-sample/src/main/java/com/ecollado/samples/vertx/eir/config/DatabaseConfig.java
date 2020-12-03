package com.ecollado.samples.vertx.eir.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseConfig implements ValidableConfig {

    private String dbname;
    private int port;
    private String host;
    private int maxPoolSize;
    private String username;
    private String password;

    public DatabaseConfig(String dbname,
                          int port,
                          String host,
                          int maxPoolSize,
                          String username,
                          String password) {
        this.dbname = dbname;
        this.port = port;
        this.host = host;
        this.maxPoolSize = maxPoolSize;
        this.username = username;
        this.password = password;
    }

    @Override
    public void validate() {
        //TODO
    }

    public String getDbname() {
        return dbname;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
