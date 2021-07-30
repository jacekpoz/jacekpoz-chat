package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginScreen implements Screen {
    private final ChatWindow window;

    private JPanel loginScreen;
    private JTextField nicknameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel result;
    private JLabel nicknameLabel;
    private JLabel passwordLabel;

    public LoginScreen(ChatWindow w) {
        window = w;
        nicknameField.addActionListener(e -> SwingUtilities.invokeLater(passwordField::requestFocusInWindow));
        ActionListener al = e -> result.setText(login(nicknameField.getText(), passwordField.getPassword()));
        passwordField.addActionListener(al);
        loginButton.addActionListener(al);
        registerButton.addActionListener(e -> window.setScreen(window.getRegisterScreen()));
    }

    private String login(String username, char[] password) {

        if (username.isEmpty() || password.length == 0) {
            return "Musisz wpisać nick i hasło";
        }

        ExecutorService service = Executors.newCachedThreadPool();

        String returned;
        try {
            returned = service.submit(() -> {
                String result = null;
                try {
                    LoginResult s = connector.login(username, password);
                    System.out.println(s);
                    switch (s) {
                        case LOGGED_IN: {
                            User u = connector.getUser(username);
                            window.getClient().setUser(u);
                            window.getOut().writeObject(u);
                            System.out.println(u + " logged in.");
                            window.setScreen(window.getMessageScreen());
                            update();
                            return "Zalogowano";
                        }
                        case ACCOUNT_DOESNT_EXIST : return "Konto o podanej nazwie nie istnieje" ;
                        case WRONG_PASSWORD       : return "Złe hasło"                           ;
                        case SQL_EXCEPTION        : return "Nie można połączyć się z serwerem"   ;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    result = "SQLException";
                } catch (IOException e) {
                    e.printStackTrace();
                    result = "IOException";
                }

                return result;
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            returned = "InterruptedException";
        } catch (ExecutionException e) {
            e.printStackTrace();
            returned = "ExecutionException";
        }

        return returned;
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

    }
}
