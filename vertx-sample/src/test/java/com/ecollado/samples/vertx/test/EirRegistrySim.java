package com.ecollado.samples.vertx.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class EirRegistrySim implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(EirRegistrySim.class);

    private ServerSocket serverSocket;

    private boolean status = false;

    private EirRegistrySimExecutor<EirSimTask> executor;

    private EirRegistrySimConfig configProperties;


    public EirRegistrySim(EirRegistrySimConfig config) {
        this.configProperties = config;
        this.executor = new EirRegistrySimExecutor<>();
    }


    @Override
    public void run() {

        this.status = true;
        try {
            serverSocket = new ServerSocket(this.configProperties.getPort());
            LOGGER.info("Server waiting for connections...");

            while (status) {
                Socket clientSocket = serverSocket.accept();

                EirSimTask handler = new EirSimTask(clientSocket);
                this.executor.execute(handler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.status = false;
    }

    public static void main(String args[]) {
        EirRegistrySimConfig eirRegistrySimConfig = new EirRegistrySimConfig();
        EirRegistrySim eirRegistrySim = new EirRegistrySim(eirRegistrySimConfig);

        eirRegistrySim.run();
    }
}
