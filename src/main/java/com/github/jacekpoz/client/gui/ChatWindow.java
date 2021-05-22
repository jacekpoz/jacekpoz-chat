package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.server.ChatSocket;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ChatWindow extends JFrame {

    private ChatSocket socket;
    private PrintWriter out;
    private BufferedReader in;

    private final MessageScreen messageScreen;
    private final LoginScreen loginScreen;
    private final RegisterScreen registerScreen;

    public ChatWindow(ChatSocket s) {
        socket = s;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        messageScreen = new MessageScreen(this);
        loginScreen = new LoginScreen(this);
        registerScreen = new RegisterScreen(this);

        setTitle("chat");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void start() {
        setScreen(loginScreen);
        setVisible(true);
    }

    public void setScreen(JPanel screen) {
        setContentPane(screen);
        pack();
    }

    public ChatSocket getSocket() {
        return socket;
    }

    public PrintWriter getOutputStream() {
        return out;
    }

    public BufferedReader getInputStream() {
        return in;
    }

    public MessageScreen getMessageScreen() {
        return messageScreen;
    }

    public LoginScreen getLoginScreen() {
        return loginScreen;
    }

    public RegisterScreen getRegisterScreen() {
        return registerScreen;
    }
}
