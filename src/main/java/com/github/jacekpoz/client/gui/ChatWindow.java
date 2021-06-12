package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.client.Client;
import com.github.jacekpoz.client.gui.screens.FriendsScreen;
import com.github.jacekpoz.client.gui.screens.LoginScreen;
import com.github.jacekpoz.client.gui.screens.MessageScreen;
import com.github.jacekpoz.client.gui.screens.RegisterScreen;
import lombok.Getter;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChatWindow extends JFrame {

    private @Getter ObjectOutputStream outputStream;
    private @Getter ObjectInputStream inputStream;

    private final @Getter MessageScreen messageScreen;
    private final @Getter LoginScreen loginScreen;
    private final @Getter RegisterScreen registerScreen;
    private final @Getter FriendsScreen friendsScreen;

    private final @Getter Client client;

    public ChatWindow(Client c) {
        client = c;

        try {
            outputStream = new ObjectOutputStream(client.getSocket().getOutputStream());
            inputStream = new ObjectInputStream(client.getSocket().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        messageScreen = new MessageScreen(this);
        loginScreen = new LoginScreen(this);
        registerScreen = new RegisterScreen(this);
        friendsScreen = new FriendsScreen(this);

        setTitle("chat");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
    }

    public void start() {
        setScreen(loginScreen);
        setVisible(true);
    }

    public void setScreen(JPanel screen) {
        setContentPane(screen);
        pack();
    }
}
