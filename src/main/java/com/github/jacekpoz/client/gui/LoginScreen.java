package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.common.GlobalStuff;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import javax.swing.*;
import java.sql.*;

public class LoginScreen extends JPanel {

    private ChatWindow window;

    public LoginScreen(ChatWindow w) {
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

        JButton loginButton = new JButton("Zaloguj");

        JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");

        loginButton.addActionListener(event -> {
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

                hash = argon2.hash(10, 65536, 1, password);

                if (!argon2.verify(hash, password)) {
                    resultLabel.setText("Nie można zweryfikować hash z hasłem");
                    return;
                }

                ResultSet rs = st.executeQuery("SELECT * FROM " + GlobalStuff.USERS_TABLE_NAME + " WHERE Username = '" + username + "';");

                if (!rs.next()) {
                    resultLabel.setText("Konto o podanej nazwie nie istnieje");
                    return;
                }

                String dbHash = rs.getString("HashedPassword");
                long userID = rs.getLong("ID");

                if (argon2.verify(dbHash, password)) {
                    argon2.wipeArray(password);

                    window.setScreen(window.getMessageScreen());
                    return;
                }
                resultLabel.setText("Złe hasło");

            } catch (SQLException e) {
                resultLabel.setText("Nie można połączyć się z serwerem");
            }
        });

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
}
