package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.client.Client;
import com.github.jacekpoz.client.gui.screens.*;
import com.google.gson.Gson;
import lombok.Getter;

import javax.swing.*;
import java.io.*;

public class ChatWindow extends JFrame {

    @Getter
    private PrintWriter out;
    @Getter
    private BufferedReader in;

    @Getter
    private final Gson gson;

    @Getter
    private final Screen[] screens;

    @Getter
    private final MessageScreen messageScreen;
    @Getter
    private final LoginScreen loginScreen;
    @Getter
    private final RegisterScreen registerScreen;
    @Getter
    private final FriendsScreen friendsScreen;
    @Getter
    private final CreateChatsScreen createChatsScreen;

    @Getter
    private final Client client;

    public ChatWindow(Client c) {
        client = c;

        try {
            out = new PrintWriter(client.getSocket().getOutputStream());
            in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        gson = new Gson();

        messageScreen = new MessageScreen(this);
        loginScreen = new LoginScreen(this);
        registerScreen = new RegisterScreen(this);
        friendsScreen = new FriendsScreen(this);
        createChatsScreen = new CreateChatsScreen(this);

        screens = new Screen[] {messageScreen, loginScreen, registerScreen, friendsScreen, createChatsScreen};

        // TODO change this after I figure out the name
        setTitle("chat");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
    }

    public void start() {
        setScreen(loginScreen);
        setVisible(true);
    }

    public void setScreen(Screen screen) {
        setContentPane(screen.getPanel());
        pack();
    }
}
