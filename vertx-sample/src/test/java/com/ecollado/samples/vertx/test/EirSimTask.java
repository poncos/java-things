package com.ecollado.samples.vertx.test;

import com.ecollado.samples.vertx.service.model.ImeiInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

class EirSimTask extends Thread {


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
                ImeiInfo imeiInfo = this.protocol.processInput(input);

                Thread.sleep( random.nextInt(1000) );
                out.writeObject(imeiInfo);

            }
        } catch (IOException ioexception) {

        } catch (InterruptedException e) {

        }
    }
}
