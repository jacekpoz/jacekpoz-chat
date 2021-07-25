package com.github.jacekpoz.client;

import com.github.jacekpoz.common.Constants;

import java.io.IOException;
import java.net.Socket;

public class ChatClientMain {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("You need to input the host and the port");
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            Client c = new Client(new Socket(host, port));
            c.start();
        } catch (IOException e) {
            System.err.println("Couldn't connect to " + Constants.SERVER_HOST);
            e.printStackTrace();
            System.exit(1);
        }

    }

}