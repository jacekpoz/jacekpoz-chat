package com.github.jacekpoz;

import com.github.jacekpoz.client.Client;
import com.github.jacekpoz.common.GlobalStuff;
import com.github.jacekpoz.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClientTest {

    private Server s;
    private Client c;

    @Before
    public void startServerAndClient() throws IOException {
        s = new Server(new ServerSocket(GlobalStuff.SERVER_PORT));
        s.start();
        c = new Client(new Socket(GlobalStuff.SERVER_HOST, GlobalStuff.SERVER_PORT));
    }

    @After
    public void stopServer() throws IOException {
        c.stop();
        s.stop();
    }

    @Test
    public void clientShouldStart() {
        c.start();

        assertTrue("Window should be visible", c.getWindow().isVisible());
    }

    @Test
    public void clientShouldHaveLoginScreenAtStartup() {
        c.start();

        assertEquals("Login screen should be the first screen at startup", c.getWindow().getContentPane(), c.getWindow().getLoginScreen());
    }
}
