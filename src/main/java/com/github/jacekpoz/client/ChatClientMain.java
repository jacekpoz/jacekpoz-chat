package com.github.jacekpoz.client;

import com.github.jacekpoz.common.GlobalStuff;

import java.io.IOException;
import java.net.Socket;

public class ChatClientMain {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Correct usage: java -jar jacekpozchat.jar <host> <port>");
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            Client c = new Client(new Socket(host, port));
            c.start();
        } catch (IOException e) {
            System.err.println("Couldn't connect to " + GlobalStuff.SERVER_HOST);
            System.exit(1);
        }

    }

}