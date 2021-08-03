package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.database.queries.user.RegisterQuery;
import com.github.jacekpoz.common.sendables.database.results.RegisterResult;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import javax.swing.*;
import java.awt.event.ActionListener;

public class RegisterScreen implements Screen {

    public static final int ID = 1;

    private transient final ChatWindow window;

    private transient JPanel registerScreen;
    private transient JTextField nicknameField;
    private transient JButton registerButton;
    private transient JButton loginButton;
    private transient JLabel result;
    private transient JPasswordField passwordField;
    private transient JLabel passwordLabel;
    private transient JLabel nicknameLabel;

    private transient final Argon2 argon2;

    public RegisterScreen(ChatWindow w) {
        window = w;
        argon2 = Argon2Factory.create();

        nicknameField.addActionListener(e -> SwingUtilities.invokeLater(passwordField::requestFocusInWindow));
        ActionListener al = e -> register(nicknameField.getText(), passwordField.getPassword());
        passwordField.addActionListener(al);
        registerButton.addActionListener(al);
        loginButton.addActionListener(e -> window.setScreen(window.getLoginScreen()));
    }

    private void register(String username, char[] password) {

        if (username.isEmpty() || password.length == 0) {
            result.setText("Musisz wpisać nick i hasło");
        }

        if (username.contains(" ")) {
            result.setText("Nick nie może zawierać spacji");
        }

        String hash = argon2.hash(10, 65536, 1, password);
        window.send(new RegisterQuery(username, hash, getScreenID()));
    }

    @Override
    public JPanel getPanel() {
        return registerScreen;
    }

    @Override
    public void update() {

    }

    @Override
    public void handleSendable(Sendable s) {
        if (s instanceof RegisterResult) {
            RegisterResult rr = (RegisterResult) s;
            switch (rr.getResult()) {
                case ACCOUNT_CREATED:
                    result.setText("Pomyślnie utworzono konto");
                    break;
                case USERNAME_TAKEN:
                    result.setText("Istnieje już użytkownik o takim nicku");
                    break;
                case SQL_EXCEPTION:
                    result.setText("Nie można połączyć się z serwerem");
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public long getScreenID() {
        return ID;
    }
}
