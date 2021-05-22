package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.common.GlobalStuff;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import javax.swing.*;
import java.sql.*;

public class RegisterScreen extends JPanel {

    private ChatWindow window;

    public RegisterScreen(ChatWindow w) {
        window = w;
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

        registerButton.addActionListener(event -> {
            if (nicknameField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                resultLabel.setText("Musisz wpisać nick i hasło");
                return;
            }
            try (Connection con = DriverManager.getConnection("jdbc:mysql://" + GlobalStuff.HOST + ":" + GlobalStuff.DB_PORT + "/" + GlobalStuff.DB_NAME, "root", "Koleganr123");
                 Statement st = con.createStatement()) {
                String username = nicknameField.getText();
                char[] password = passwordField.getPassword();

                Argon2 argon2 = Argon2Factory.create();
                String hash;

                try {
                    hash = argon2.hash(10, 65536, 1, password);

                    if (!argon2.verify(hash, password)) {
                        resultLabel.setText("Nie można zweryfikować hash z hasłem");
                        return;
                    }
                } finally {
                    argon2.wipeArray(password);
                }


                ResultSet rs = st.executeQuery("SELECT Username FROM " + GlobalStuff.USERS_TABLE_NAME + " WHERE Username = '" + username + "';");
                if (rs.next()) {
                    resultLabel.setText("Istnieje użytkownik o takiej nazwie");
                    return;
                }

                st.execute("INSERT INTO users (Username, HashedPassword, ID) VALUES ('" + username + "', '" + hash + "', " + GlobalStuff.USER_COUNTER++ + ");");
                resultLabel.setText("Pomyślnie utworzono konto");

            } catch (SQLException e) {
                resultLabel.setText("Nie można połączyć się z serwerem");
                e.printStackTrace();
            }
        });

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
}
