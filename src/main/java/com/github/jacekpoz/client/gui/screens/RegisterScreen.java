package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.DatabaseConnector;
import com.github.jacekpoz.common.GlobalStuff;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class RegisterScreen extends JPanel {

    private ChatWindow window;
    private boolean isRegistering;

    public RegisterScreen(ChatWindow w) {
        window = w;
        isRegistering = false;
        initUI();
    }

    private void initUI() {
        JLabel nicknameLabel = new JLabel("Nazwa:");

        JTextField nicknameField = new JTextField();

        nicknameLabel.setLabelFor(nicknameField);

        JLabel passwordLabel = new JLabel("Hasło:");

        JPasswordField passwordField = new JPasswordField();

        passwordLabel.setLabelFor(passwordField);

        JButton registerButton = new JButton("Zarejestruj");

        JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");

        ActionListener al = event -> resultLabel.setText(register(nicknameField.getText(), passwordField.getPassword()));

        nicknameField.addActionListener(event -> SwingUtilities.invokeLater(passwordField::requestFocusInWindow));
        passwordField.addActionListener(al);
        registerButton.addActionListener(al);

        JButton loginLabel = new JButton("Logowanie");
        loginLabel.addActionListener(e -> window.setScreen(window.getLoginScreen()));

        createLayout(nicknameLabel, nicknameField, passwordLabel, passwordField, registerButton, resultLabel, loginLabel);

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

    private String register(String username, char[] password) {
        isRegistering = true;

        if (username.isEmpty() || password.length == 0) {
            return "Musisz wpisać nick i hasło";
        }

        if (username.contains(" ")) {
            return "Nick nie może zawierać spacji";
        }

        AtomicReference<String> result = new AtomicReference<>();

        ExecutorService service = Executors.newCachedThreadPool();

        service.submit(() -> {
            try {
                DatabaseConnector connector = new DatabaseConnector(
                        "jdbc:mysql://localhost:3306/" + GlobalStuff.DB_NAME,
                        "chat-client", "DB_Password_0123456789"
                );

                switch (connector.register(username, password)) {
                    case ACCOUNT_CREATED : result.set("Pomyślnie utworzono konto"           ); break;
                    case USERNAME_TAKEN  : result.set("Istnieje użytkownik o takiej nazwie" ); break;
                    case SQL_EXCEPTION   : result.set("Nie można połączyć się z serwerem"   ); break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                result.set("Nie można połączyć się z serwerem");
            }
        });

        isRegistering = false;

        return result.get();
    }
}
