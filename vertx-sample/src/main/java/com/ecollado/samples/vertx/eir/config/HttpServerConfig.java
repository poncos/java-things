package com.ecollado.samples.vertx.eir.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpServerConfig implements ValidableConfig{

    private int port;

    private String bind;

    public HttpServerConfig(int aPort, String aBindAddress) {
        this.port = aPort;
        this.bind = aBindAddress;
    }

    @Override
    public void validate() {
        // TODO
    }

    public int getPort() {
        return port;
    }

    public String getBind() {
        return bind;
    }
}
