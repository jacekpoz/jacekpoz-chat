package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.DatabaseConnector;
import com.github.jacekpoz.common.UserInfo;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class LoginScreen extends JPanel {

    private ChatWindow window;
    private boolean isLoggingIn;

    public LoginScreen(ChatWindow w) {
        window = w;
        isLoggingIn = false;
        initUI();
    }

    private void initUI() {
        JLabel nicknameLabel = new JLabel("Nazwa:");

        JTextField nicknameField = new JTextField();

        nicknameLabel.setLabelFor(nicknameField);

        JLabel passwordLabel = new JLabel("Hasło:");

        JPasswordField passwordField = new JPasswordField();

        passwordLabel.setLabelFor(passwordField);

        JButton loginButton = new JButton("Zaloguj");

        JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");

        ActionListener al = a -> {
            if (!isLoggingIn) resultLabel.setText(login(nicknameField.getText(), passwordField.getPassword()));
        };

        nicknameField.addActionListener(a -> SwingUtilities.invokeLater(passwordField::requestFocusInWindow));
        passwordField.addActionListener(al);
        loginButton.addActionListener(al);

        JButton registerLabel = new JButton("Rejestracja");
        registerLabel.addActionListener(e -> window.setScreen(window.getRegisterScreen()));

        createLayout(nicknameLabel, nicknameField, passwordLabel, passwordField, loginButton, resultLabel, registerLabel);

    }

    private void createLayout(JComponent... args) {
        GroupLayout gl = new GroupLayout(this);
        setLayout(gl);

        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup()
                .addGap(300)
                .addComponent(args[0])
                .addComponent(args[1])
                .addComponent(args[2])
                .addComponent(args[3])
                .addComponent(args[4])
                .addComponent(args[5])
                .addComponent(args[6])
                .addGap(300)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(args[0])
                .addComponent(args[1])
                .addComponent(args[2])
                .addComponent(args[3])
                .addComponent(args[4])
                .addComponent(args[5])
                .addComponent(args[6])
        );
    }

    private String login(String username, char[] password) {

        isLoggingIn = true;

        if (username.isEmpty() || password.length == 0) {
            return "Musisz wpisać nick i hasło";
        }

        AtomicReference<String> result = new AtomicReference<>();

        ExecutorService service = Executors.newCachedThreadPool();

        service.submit(() -> {
            try {
                DatabaseConnector connector = new DatabaseConnector(
                        "jdbc:mysql://localhost:3306/mydatabase",
                        "chat-client", "DB_Password_0123456789"
                );

                switch (connector.login(username, password)) {
                    case LOGGED_IN: {
                        UserInfo u = connector.getUserInfo(username);
                        window.getClient().setUser(u);
                        window.getOutputStream().writeObject(u);
                        System.out.println(u + " logged in.");
                        window.setScreen(window.getMessageScreen());
                        break;
                    }
                    case ACCOUNT_DOESNT_EXIST : result.set("Konto o podanej nazwie nie istnieje" ); break;
                    case WRONG_PASSWORD       : result.set("Złe hasło"                           ); break;
                    case SQL_EXCEPTION        : result.set("Nie można połączyć się z serwerem"   ); break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                result.set("SQLException");
            } catch (IOException e) {
                e.printStackTrace();
                result.set("IOException");
            }
        });

        isLoggingIn = false;

        return result.get();
    }
}
