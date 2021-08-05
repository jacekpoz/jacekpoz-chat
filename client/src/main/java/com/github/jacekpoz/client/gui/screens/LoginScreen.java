package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.client.gui.Screen;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.user.LoginQuery;
import com.github.jacekpoz.common.sendables.database.results.LoginResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginScreen implements Screen {

    private final static Logger LOGGER = Logger.getLogger(LoginScreen.class.getName());

    private transient final ChatWindow window;

    private transient JPanel loginScreen;
    private transient JTextField nicknameField;
    private transient JPasswordField passwordField;
    private transient JButton loginButton;
    private transient JButton registerButton;
    private transient JLabel result;
    private transient JLabel nicknameLabel;
    private transient JLabel passwordLabel;
    private JButton settingsButton;

    public LoginScreen(ChatWindow w) {
        window = w;
        nicknameField.addActionListener(e -> SwingUtilities.invokeLater(passwordField::requestFocusInWindow));
        ActionListener al = e -> login(nicknameField.getText(), passwordField.getPassword());
        passwordField.addActionListener(al);
        loginButton.addActionListener(al);
        registerButton.addActionListener(e -> window.setScreen(window.getRegisterScreen()));

        settingsButton.addActionListener(e -> {
            window.setScreen(window.getSettingsScreen());
            window.getSettingsScreen().setLastScreen(this);
        });
    }

    private void login(String username, char[] password) {

        if (username.isEmpty() || password.length == 0) {
            result.setText("Musisz wpisać nick i hasło");
            return;
        }

        window.send(new LoginQuery(username, new String(password).getBytes(StandardCharsets.UTF_8), getScreenID()));
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
                    window.setScreen(window.getMessageScreen());
                    update();
                    result.setText(window.getLanguageBundle().getString("app.logged_in"));
                    LOGGER.log(Level.INFO, "Logged in", u);
                    break;
                }
                case ACCOUNT_DOESNT_EXIST:
                    LOGGER.log(Level.INFO, "Account doesn't exist");
                    result.setText(window.getLanguageBundle().getString("app.account_doesnt_exist"));
                    break;
                case WRONG_PASSWORD:
                    LOGGER.log(Level.INFO, "Wrong password");
                    result.setText(window.getLanguageBundle().getString("app.wrong_password"));
                    break;
                case SQL_EXCEPTION:
                    LOGGER.log(Level.SEVERE, "An SQLException occured while logging in ", lr.getEx());
                    result.setText(window.getLanguageBundle().getString("app.sql_exception"));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public long getScreenID() {
        return 0;
    }

    @Override
    public void changeLanguage() {
        nicknameLabel.setText(window.getLangString("app.nickname"));
        passwordLabel.setText(window.getLangString("app.password"));
        loginButton.setText(window.getLangString("app.login"));
        registerButton.setText(window.getLangString("app.go_to_register"));
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
        loginScreen = new JPanel();
        loginScreen.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        loginScreen.setBackground(new Color(-12829636));
        loginScreen.setForeground(new Color(-1));
        nicknameLabel = new JLabel();
        nicknameLabel.setBackground(new Color(-12829636));
        nicknameLabel.setEnabled(true);
        nicknameLabel.setForeground(new Color(-1));
        this.$$$loadLabelText$$$(nicknameLabel, this.$$$getMessageFromBundle$$$("lang", "app.nickname"));
        loginScreen.add(nicknameLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setBackground(new Color(-12829636));
        passwordLabel.setEnabled(true);
        passwordLabel.setForeground(new Color(-1));
        this.$$$loadLabelText$$$(passwordLabel, this.$$$getMessageFromBundle$$$("lang", "app.password"));
        loginScreen.add(passwordLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(-12829636));
        passwordField.setForeground(new Color(-1));
        loginScreen.add(passwordField, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        loginButton = new JButton();
        loginButton.setBackground(new Color(-12829636));
        loginButton.setForeground(new Color(-1));
        this.$$$loadButtonText$$$(loginButton, this.$$$getMessageFromBundle$$$("lang", "app.login"));
        loginScreen.add(loginButton, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registerButton = new JButton();
        registerButton.setBackground(new Color(-12829636));
        registerButton.setForeground(new Color(-1));
        this.$$$loadButtonText$$$(registerButton, this.$$$getMessageFromBundle$$$("lang", "app.go_to_register"));
        loginScreen.add(registerButton, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nicknameField = new JTextField();
        nicknameField.setBackground(new Color(-12829636));
        nicknameField.setForeground(new Color(-1));
        loginScreen.add(nicknameField, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        result = new JLabel();
        result.setBackground(new Color(-12829636));
        result.setForeground(new Color(-1));
        result.setText("");
        loginScreen.add(result, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        settingsButton = new JButton();
        settingsButton.setBackground(new Color(-12829636));
        settingsButton.setEnabled(true);
        settingsButton.setForeground(new Color(-1));
        settingsButton.setHorizontalAlignment(0);
        settingsButton.setIcon(new ImageIcon(getClass().getResource("/images/settings.png")));
        settingsButton.setText("");
        loginScreen.add(settingsButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nicknameLabel.setLabelFor(nicknameField);
        passwordLabel.setLabelFor(passwordField);
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return loginScreen;
    }

}
