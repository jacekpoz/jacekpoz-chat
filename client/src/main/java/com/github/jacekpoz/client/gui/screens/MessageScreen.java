package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.*;
import com.github.jacekpoz.client.gui.Screen;
import com.github.jacekpoz.common.Util;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.chat.GetUsersChatsQuery;
import com.github.jacekpoz.common.sendables.database.queries.message.InsertMessageQuery;
import com.github.jacekpoz.common.sendables.database.queries.user.GetMessageAuthorQuery;
import com.github.jacekpoz.common.sendables.database.results.ChatResult;
import com.github.jacekpoz.common.sendables.database.results.UserResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class MessageScreen implements Screen {

    public static final int ID = 2;

    private transient final ChatWindow window;

    private transient JPanel messageScreen;
    private transient JButton chatsButton;
    private transient JTextField messageField;
    private transient JButton sendMessageButton;
    private transient JButton friendsButton;
    private transient MessageContainer messages;
    private transient JScrollPane messagesScrollPane;
    private transient ChatContainer chats;
    private transient JScrollPane chatsScrollPane;

    private List<Chat> usersChats;
    private Map<Long, User> messageAuthors;

    public MessageScreen(ChatWindow w) {
        window = w;
        usersChats = new ArrayList<>();
        messageAuthors = new HashMap<>();

        ActionListener sendMessageAction = e -> {
            if (!messageField.getText().isEmpty()) {
                Chat c = window.getClient().getChat();
                Message m = new Message(
                        c.getMessages().size(),
                        c.getId(),
                        window.getClient().getUser().getId(),
                        messageField.getText(),
                        Timestamp.valueOf(LocalDateTime.now())
                );
                c.getMessages().add(m);
                sendMessage(m);
                messageField.setText("");
                JScrollBar bar = messagesScrollPane.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            }
        };

        messageField.addActionListener(sendMessageAction);
        sendMessageButton.addActionListener(sendMessageAction);
        friendsButton.addActionListener(e -> window.setScreen(window.getFriendsScreen()));
        chatsButton.addActionListener(e -> window.setScreen(window.getCreateChatsScreen()));
    }

    private void sendMessage(Message message) {
        System.out.println("sendMessage: " + message);
        window.send(message);
        window.send(new InsertMessageQuery(
                message.getMessageID(),
                message.getChatID(),
                message.getAuthorID(),
                message.getContent(),
                getScreenID()
        ));
    }

    public void setChat(Chat c) {
        window.getClient().setChat(c);
        window.send(c);
        messages.removeAllMessages();
        c.getMessages().forEach(message ->
                messages.addMessage(new MessagePanel(messageAuthors.get(message.getMessageID()), message)));
        messageField.setEnabled(true);
        sendMessageButton.setEnabled(true);
        messages.revalidate();
    }

    @Override
    public JPanel getPanel() {
        return messageScreen;
    }

    @Override
    public void update() {
        window.send(new GetUsersChatsQuery(window.getClient().getUser().getId(), getScreenID()));

        chats.removeAllChats();
        for (Chat c : usersChats) {
            for (Message m : c.getMessages()) {
                window.send(new GetMessageAuthorQuery(m.getMessageID(), m.getAuthorID(), getScreenID()));
            }

            ChatPanel cp = new ChatPanel(this, chats, c);
            cp.setToolTipText(Util.userListToString(c.getMembers()));
            chats.addChat(cp);
            System.out.println("added chat: " + c);
        }
    }

    @Override
    public void handleSendable(Sendable s) {
        if (s instanceof ChatResult) {
            System.out.println("MessageScreen ChatResult");
            ChatResult cr = (ChatResult) s;
            if (cr.getQuery() instanceof GetUsersChatsQuery) {
                usersChats = cr.get();
            }
        } else if (s instanceof UserResult) {
            System.out.println("MessageScreen UserResult");
            UserResult ur = (UserResult) s;
            if (ur.getQuery() instanceof GetMessageAuthorQuery) {
                GetMessageAuthorQuery gmaq = (GetMessageAuthorQuery) ur.getQuery();
                messageAuthors.put(gmaq.getMessageID(), ur.get().get(0));
            }
        } else if (s instanceof Message) {
            System.out.println("MessageScreen Message");
            Message m = (Message) s;
            SwingUtilities.invokeLater(() ->
                    messages.addMessage(new MessagePanel(messageAuthors.get(m.getMessageID()), m)));
        }

    }

    @Override
    public long getScreenID() {
        return ID;
    }

    @Override
    public void changeLanguage() {
        ResourceBundle lang = window.getLanguageBundle();
        sendMessageButton.setText(lang.getString("send"));
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
        messageScreen = new JPanel();
        messageScreen.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        messageScreen.setBackground(new Color(-12829636));
        messageScreen.setForeground(new Color(-1));
        chatsButton = new JButton();
        chatsButton.setBackground(new Color(-12829636));
        chatsButton.setForeground(new Color(-1));
        chatsButton.setText("C");
        messageScreen.add(chatsButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        messageField = new JTextField();
        messageField.setBackground(new Color(-12829636));
        messageField.setEditable(true);
        messageField.setEnabled(false);
        messageField.setForeground(new Color(-1));
        messageScreen.add(messageField, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        sendMessageButton = new JButton();
        sendMessageButton.setBackground(new Color(-12829636));
        sendMessageButton.setEnabled(false);
        sendMessageButton.setForeground(new Color(-1));
        this.$$$loadButtonText$$$(sendMessageButton, this.$$$getMessageFromBundle$$$("lang", "send"));
        messageScreen.add(sendMessageButton, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        messagesScrollPane = new JScrollPane();
        messagesScrollPane.setBackground(new Color(-12829636));
        messagesScrollPane.setForeground(new Color(-1));
        messagesScrollPane.setVerticalScrollBarPolicy(22);
        messageScreen.add(messagesScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 2, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(500, 250), null, 0, false));
        messages = new MessageContainer();
        messages.setBackground(new Color(-12829636));
        messages.setForeground(new Color(-1));
        messagesScrollPane.setViewportView(messages);
        chatsScrollPane = new JScrollPane();
        chatsScrollPane.setBackground(new Color(-12829636));
        chatsScrollPane.setForeground(new Color(-1));
        chatsScrollPane.setVerticalScrollBarPolicy(22);
        messageScreen.add(chatsScrollPane, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 2, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(100, 250), null, 0, false));
        chats = new ChatContainer();
        chats.setBackground(new Color(-12829636));
        chats.setForeground(new Color(-1));
        chatsScrollPane.setViewportView(chats);
        friendsButton = new JButton();
        friendsButton.setBackground(new Color(-12829636));
        friendsButton.setForeground(new Color(-1));
        friendsButton.setText("F");
        messageScreen.add(friendsButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return messageScreen;
    }

}
