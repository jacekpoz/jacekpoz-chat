package com.github.jacekpoz;

import com.github.jacekpoz.client.Client;
import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.Message;
import com.github.jacekpoz.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    private Server s;
    private Client c;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    @Before
    public void initializeStreams() throws IOException {
        s = new Server(new ServerSocket(Constants.SERVER_PORT));
        c = new Client(new Socket(Constants.SERVER_HOST, Constants.SERVER_PORT));
        out = c.getWindow().getOutputStream();
        in = c.getWindow().getInputStream();
    }

    @After
    public void closeStreams() throws IOException {
        if (out != null) out.close();
        if (in != null) in.close();
        c.stop();
        s.stop();
    }

    @Test
    public void messageShouldBeSentBetweenSockets() throws IOException, ClassNotFoundException {
        Message input = new Message("dupa dupa 1234");
        Message output;
        out.writeObject(input);
        output = (Message) in.readObject();

        assertEquals("Input and output messages should be equal", input, output);
    }
    
}
