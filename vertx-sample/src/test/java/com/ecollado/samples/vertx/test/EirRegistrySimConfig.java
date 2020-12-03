package com.ecollado.samples.vertx.test;

import java.util.Optional;
import java.util.Properties;

public class EirRegistrySimConfig {

    private static final String SERVER_PORT_NUMBER = "eirregistrysim.port";

    private static final String DEFAULT_PORT_VALUE = "9999";


    private Properties properties;

    public EirRegistrySimConfig() {
        this.properties = new Properties();
    }

    public Integer getPort() {
        String property = this.properties.getProperty(EirRegistrySimConfig.SERVER_PORT_NUMBER, EirRegistrySimConfig.DEFAULT_PORT_VALUE);

        return Integer.parseInt(property);
    }

}
