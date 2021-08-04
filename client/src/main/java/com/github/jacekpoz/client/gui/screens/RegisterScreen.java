package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.database.queries.user.RegisterQuery;
import com.github.jacekpoz.common.sendables.database.results.RegisterResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import static com.kosprov.jargon2.api.Jargon2.*;

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

    public RegisterScreen(ChatWindow w) {
        window = w;

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

        Hasher hasher = jargon2Hasher()
                .type(Type.ARGON2d)
                .memoryCost(65536)
                .timeCost(3)
                .parallelism(4)
                .saltLength(16)
                .hashLength(64);

        String hash = hasher
                .password(new String(password).getBytes(StandardCharsets.UTF_8))
                .encodedHash();

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
            System.out.println("RegisterScreen RegisterResult");
            RegisterResult rr = (RegisterResult) s;
            switch (rr.getResult()) {
                case ACCOUNT_CREATED:
                    System.out.println("RegisterScreen ACCOUNT_CREATED");
                    result.setText("Pomyślnie utworzono konto");
                    break;
                case USERNAME_TAKEN:
                    System.out.println("RegisterScreen USERNAME_TAKEN");
                    result.setText("Istnieje już użytkownik o takim nicku");
                    break;
                case SQL_EXCEPTION:
                    System.out.println("RegisterScreen SQL_EXCEPTION");
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        registerScreen = new JPanel();
        registerScreen.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 3, new Insets(0, 0, 0, 0), -1, -1));
        registerScreen.setBackground(new Color(-12829636));
        registerScreen.setForeground(new Color(-12829636));
        nicknameLabel = new JLabel();
        nicknameLabel.setBackground(new Color(-12829636));
        nicknameLabel.setForeground(new Color(-1));
        nicknameLabel.setText("Nazwa:");
        registerScreen.add(nicknameLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nicknameField = new JTextField();
        nicknameField.setBackground(new Color(-12829636));
        nicknameField.setForeground(new Color(-1));
        registerScreen.add(nicknameField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setBackground(new Color(-12829636));
        passwordLabel.setForeground(new Color(-1));
        passwordLabel.setText("Hasło:");
        registerScreen.add(passwordLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registerButton = new JButton();
        registerButton.setBackground(new Color(-12829636));
        registerButton.setForeground(new Color(-1));
        registerButton.setText("Zarejestruj");
        registerScreen.add(registerButton, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loginButton = new JButton();
        loginButton.setBackground(new Color(-12829636));
        loginButton.setForeground(new Color(-1));
        loginButton.setText("Logowanie");
        registerScreen.add(loginButton, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        result = new JLabel();
        result.setBackground(new Color(-12829636));
        result.setForeground(new Color(-1));
        result.setText("");
        registerScreen.add(result, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(-12829636));
        passwordField.setForeground(new Color(-1));
        registerScreen.add(passwordField, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        nicknameLabel.setLabelFor(nicknameField);
        passwordLabel.setLabelFor(passwordField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return registerScreen;
    }
}
