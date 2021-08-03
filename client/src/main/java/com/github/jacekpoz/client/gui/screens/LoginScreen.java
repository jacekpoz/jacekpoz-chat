package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.user.LoginQuery;
import com.github.jacekpoz.common.sendables.database.results.LoginResult;

import javax.swing.*;
import java.awt.event.ActionListener;

public class LoginScreen implements Screen {

    public static final int ID = 0;

    private transient final ChatWindow window;

    private transient JPanel loginScreen;
    private transient JTextField nicknameField;
    private transient JPasswordField passwordField;
    private transient JButton loginButton;
    private transient JButton registerButton;
    private transient JLabel result;
    private transient JLabel nicknameLabel;
    private transient JLabel passwordLabel;

    public LoginScreen(ChatWindow w) {
        window = w;
        nicknameField.addActionListener(e -> SwingUtilities.invokeLater(passwordField::requestFocusInWindow));
        ActionListener al = e -> login(nicknameField.getText(), passwordField.getPassword());
        passwordField.addActionListener(al);
        loginButton.addActionListener(al);
        registerButton.addActionListener(e -> window.setScreen(window.getRegisterScreen()));
    }

    private void login(String username, char[] password) {

        if (username.isEmpty() || password.length == 0) {
            result.setText("Musisz wpisać nick i hasło");
            return;
        }

        window.send(new LoginQuery(username, password, getScreenID()));
    }

    @Override
    public JPanel getPanel() {
        return loginScreen;
    }

    @Override
    public void update() {
        for (Screen s : window.getScreens())
            if (!(s instanceof LoginScreen))
                s.update();
    }

    @Override
    public void handleSendable(Sendable s) {
        if (s instanceof LoginResult) {
            LoginResult lr = (LoginResult) s;
            switch (lr.getResult()) {
                case LOGGED_IN: {
                    User u = lr.get().get(0);
                    window.getClient().setUser(u);
                    window.send(u);
                    System.out.println(u + " logged in.");
                    window.setScreen(window.getMessageScreen());
                    update();
                    result.setText("Zalogowano");
                    break;
                }
                case ACCOUNT_DOESNT_EXIST:
                    result.setText("Konto o podanej nazwie nie istnieje");
                    break;
                case WRONG_PASSWORD:
                    result.setText("Złe hasło");
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
