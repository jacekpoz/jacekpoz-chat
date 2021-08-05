package com.github.jacekpoz.client;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.*;

public class ChatClientMain {


    private final static Logger LOGGER = Logger.getLogger(ChatClientMain.class.getName());

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("You need to input the host and the port");
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
                LOGGER.log(Level.SEVERE, t + "a RuntimeException occured", e));

        try {
            Client c = new Client(new Socket(host, port));
            c.start();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to server", e);
            System.exit(1);
        }

    }

}