package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.client.Client;
import com.github.jacekpoz.client.InputHandler;
import com.github.jacekpoz.client.gui.screens.*;
import com.github.jacekpoz.client.logging.LogFormatter;
import com.github.jacekpoz.common.gson.LocalDateTimeAdapter;
import com.github.jacekpoz.common.gson.SendableAdapter;
import com.github.jacekpoz.common.sendables.Sendable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.*;

public class ChatWindow extends JFrame {

    private final static Logger ROOT_LOGGER = LogManager.getLogManager().getLogger("");
    private final static Logger LOGGER = Logger.getLogger(ChatWindow.class.getName());

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

    @Getter
    private String logDirectory;

    @Getter
    private String currentLogFile;

    public ChatWindow(Client c) {
        client = c;

        languageBundle = ResourceBundle.getBundle("lang");
        logDirectory = ".";
        ROOT_LOGGER.setUseParentHandlers(false);
        changeLogDirectory(logDirectory);

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
        setTitle(languageBundle.getString("app.title"));
        for (Screen s : screens) {
            s.changeLanguage();
            pack();
        }
    }

    public void changeLogDirectory(String logDirectory) {
        this.logDirectory = logDirectory;
        try {
            for (Handler h : ROOT_LOGGER.getHandlers()) {
                ROOT_LOGGER.removeHandler(h);
                h.flush();
                h.close();
            }

            String logFile = logDirectory + "\\jacekpozchat_" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")
                    .format(new Date(System.currentTimeMillis())) + ".log";
            FileHandler fh = new FileHandler(logFile);
            currentLogFile = logFile;
            fh.setFormatter(new LogFormatter());
            ROOT_LOGGER.addHandler(fh);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to create log file", e);
        }
    }

    public String getLangString(String key) {
        return languageBundle.getString(key);
    }
}
