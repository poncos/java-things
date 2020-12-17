package com.ecollado.samples.keystore;

import java.security.KeyStore;

public class App {

    private static final String DEFAULT_TRUSTSTORE_FILE = "/tmp/truststore";

    //private static final String DEFAULT_CERTIFICATE_TYPE = "X.509";

    private static final String DEFAULT_KEYSOTRE_FILE = "/tmp/keystore";

    public static void main(String args[])
    {
        ECKeyPairGenerator ecKeyPairGenerator = new ECKeyPairGenerator();

        KeyStore keyStore = ecKeyPairGenerator.load(DEFAULT_KEYSOTRE_FILE);

        ecKeyPairGenerator.save(keyStore, DEFAULT_KEYSOTRE_FILE);
        KeyStore trustStore = ecKeyPairGenerator.createTrustStore(keyStore);
        ecKeyPairGenerator.save(trustStore, DEFAULT_TRUSTSTORE_FILE);
    }
}
