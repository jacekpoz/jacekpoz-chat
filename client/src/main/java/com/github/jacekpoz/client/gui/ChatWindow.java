package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.client.Client;
import com.github.jacekpoz.client.InputHandler;
import com.github.jacekpoz.client.gui.screens.*;
import com.github.jacekpoz.common.gson.LocalDateTimeAdapter;
import com.github.jacekpoz.common.gson.SendableAdapter;
import com.github.jacekpoz.common.sendables.Sendable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class ChatWindow extends JFrame {

    @Getter
    private PrintWriter out;
    @Getter
    private BufferedReader in;

    @Getter
    private final Gson gson;
    @Getter
    private final InputHandler handler;

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
    private final SettingsScreen settingsScreen;

    @Getter
    private final Client client;

    @Getter
    @Setter
    private ResourceBundle languageBundle;

    public ChatWindow(Client c) {
        client = c;

        languageBundle = ResourceBundle.getBundle("lang");

        try {
            out = new PrintWriter(client.getSocket().getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        gson = new GsonBuilder()
                .registerTypeAdapter(Sendable.class, new SendableAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        handler = new InputHandler(this);

        messageScreen = new MessageScreen(this);
        loginScreen = new LoginScreen(this);
        registerScreen = new RegisterScreen(this);
        friendsScreen = new FriendsScreen(this);
        createChatsScreen = new CreateChatsScreen(this);
        settingsScreen = new SettingsScreen(this);

        screens = new Screen[] {messageScreen, loginScreen, registerScreen, friendsScreen, createChatsScreen, settingsScreen};

        // TODO change this after I figure out the name
        changeLanguage(Locale.US);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        handler.start();
    }

    public void start() {
        setScreen(loginScreen);
        setVisible(true);
    }

    public void setScreen(Screen screen) {
        setContentPane(screen.getPanel());
        pack();
    }

    public void send(Sendable s) {
        String json = gson.toJson(s, Sendable.class);
        out.println(json);
    }

    public Screen getScreen(long id) {
        for (Screen s : screens)
            if (s.getScreenID() == id)
                return s;
        return null;
    }

    public void changeLanguage(Locale lang) {
        languageBundle = ResourceBundle.getBundle("lang", lang);
        setTitle(languageBundle.getString("application.title"));
        for (Screen s : screens)
            s.changeLanguage();
    }
}
