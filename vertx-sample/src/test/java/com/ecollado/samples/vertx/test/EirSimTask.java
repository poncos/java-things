package com.ecollado.samples.vertx.test;

import com.ecollado.samples.vertx.eir.model.ImeiInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

class EirSimTask extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(EirSimTask.class);

    private EirSimProtocol protocol;

    private Socket clientSocket;


    public EirSimTask(Socket socket) {
        this.clientSocket = socket;
        this.protocol = new EirSimProtocol();
    }

    @Override
    public void run() {

        try {
            Random random = new Random();
            ObjectOutputStream out =
                    new ObjectOutputStream(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String input;
            while ((input = in.readLine()) != null) {
                LOGGER.debug("Received: [{}]",input);
                ImeiInfo imeiInfo = this.protocol.processInput(input);

                try {
                    Thread.sleep(random.nextInt(1000));
                } catch(InterruptedException iex) {}

                out.writeObject(imeiInfo);
                LOGGER.debug("Sent: [{}]",imeiInfo);

            }
        } catch (IOException ioexception) {
            LOGGER.error("i/0 error",ioexception);
        }
    }
}
