package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.DatabaseConnector;
import com.github.jacekpoz.common.Constants;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.jacekpoz.common.DatabaseConnector.*;

public class RegisterScreen implements Screen {
    private final ChatWindow window;

    private JPanel registerScreen;
    private JTextField nicknameField;
    private JButton registerButton;
    private JButton loginButton;
    private JLabel result;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JLabel nicknameLabel;

    public RegisterScreen(ChatWindow w) {
        window = w;
        nicknameField.addActionListener(e -> SwingUtilities.invokeLater(passwordField::requestFocusInWindow));
        ActionListener al = e -> result.setText(register(nicknameField.getText(), passwordField.getPassword()));
        passwordField.addActionListener(al);
        registerButton.addActionListener(al);
        loginButton.addActionListener(e -> window.setScreen(window.getLoginScreen()));
    }

    private String register(String username, char[] password) {

        if (username.isEmpty() || password.length == 0) {
            return "Musisz wpisać nick i hasło";
        }

        if (username.contains(" ")) {
            return "Nick nie może zawierać spacji";
        }


        ExecutorService service = Executors.newCachedThreadPool();

        String returned;
        try {
            returned = service.submit(() -> {
                String result = null;
                try {
                    DatabaseConnector connector = new DatabaseConnector(
                            "jdbc:mysql://localhost:3306/" + Constants.DB_NAME,
                            "chat-client", "DB_Password_0123456789"
                    );

                    switch (connector.register(username, password)) {
                        case ACCOUNT_CREATED : result = "Pomyślnie utworzono konto"           ; break;
                        case USERNAME_TAKEN  : result = "Istnieje użytkownik o takiej nazwie" ; break;
                        case SQL_EXCEPTION   : result = "Nie można połączyć się z serwerem"   ; break;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    result = "SQLException";
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
        return registerScreen;
    }

    @Override
    public void update() {

    }
}