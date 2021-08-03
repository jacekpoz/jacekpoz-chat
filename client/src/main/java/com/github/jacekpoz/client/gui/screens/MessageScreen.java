package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.*;
import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.Util;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.chat.GetUsersChatsQuery;
import com.github.jacekpoz.common.sendables.database.queries.user.GetMessageAuthorQuery;
import com.github.jacekpoz.common.sendables.database.results.ChatResult;
import com.github.jacekpoz.common.sendables.database.results.UserResult;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Message, User> messageAuthors;

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
        String json = window.getGson().toJson(message, message.getClass());
        window.getOut().println(json);
    }

    public void setChat(Chat c) {
        String jsonChat = window.getGson().toJson(c, c.getClass());
        window.getClient().setChat(c);
        window.getOut().println(jsonChat);
        messages.removeAllMessages();
        c.getMessages().forEach(message ->
                messages.addMessage(new MessagePanel(messageAuthors.get(message), message)));
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
                window.send(new GetMessageAuthorQuery(m, getScreenID()));
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
            ChatResult cr = (ChatResult) s;
            if (cr.getQuery() instanceof GetUsersChatsQuery) {
                usersChats = cr.get();
            }
        } else if (s instanceof UserResult) {
            UserResult ur = (UserResult) s;
            if (ur.getQuery() instanceof GetMessageAuthorQuery) {
                GetMessageAuthorQuery gmaq = (GetMessageAuthorQuery) ur.getQuery();
                messageAuthors.put(gmaq.getMessage(), ur.get().get(0));
            }
        } else if (s instanceof Message) {
            Message m = (Message) s;
            SwingUtilities.invokeLater(() ->
                    messages.addMessage(new MessagePanel(messageAuthors.get(m), m)));
        }

    }

    @Override
    public long getScreenID() {
        return ID;
    }
}
