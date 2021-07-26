package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.*;
import com.github.jacekpoz.common.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageScreen implements Screen {
    private final ChatWindow window;

    private JPanel messageScreen;
    private JButton chatsButton;
    private JTextField messageField;
    private JButton sendMessageButton;
    private JButton friendsButton;
    private MessageContainer messages;
    private JScrollPane messagesScrollPane;
    private ChatContainer chats;
    private JScrollPane chatsScrollPane;

    public MessageScreen(ChatWindow w) {
        window = w;

        ActionListener sendMessageAction = e -> {
            if (!messageField.getText().isEmpty()) {
                Chat c = window.getClient().getChat();
                Message m = new Message(
                        c.getMessageIDs().size(),
                        c.getId(),
                        window.getClient().getUser().getId(),
                        messageField.getText(),
                        Timestamp.valueOf(LocalDateTime.now())
                );
                c.getMessageIDs().add(m.getMessageID());
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

        ExecutorService service = Executors.newCachedThreadPool();

        service.submit(() -> {
            while (true) {
                try {
                    Sendable jsonFromServer = window.getGson().fromJson(window.getIn().readLine(), Sendable.class);
                    Message fromServer = (Message) jsonFromServer;

                    SwingUtilities.invokeLater(() -> messages.addMessage(
                            new MessagePanel(), fromServer)
                    ));

                } catch (EOFException e) {
                    System.err.println("EOF");
                    e.printStackTrace();
                    break;
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMessage(Message message) {
        System.out.println("sendMessage: " + message);
        String json = window.getGson().toJson(message, message.getClass());
        window.getOut().println(json);
    }

    public void setChat(Chat c) {
        String jsonChat = window.getGson().toJson(c, c.getClass());
        try {
            window.getClient().setChat(c);
            window.getOut().println(jsonChat);
            messages.removeAllMessages();
            c.getMessageIDs().forEach(mID -> {
                Message m = connector.getMessage(mID, c.getId());
                User author = connector.getUser(m.getAuthorID());
                messages.addMessage(new MessagePanel(author, m));
            });
            messageField.setEnabled(true);
            sendMessageButton.setEnabled(true);
            messages.revalidate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JPanel getPanel() {
        return messageScreen;
    }

    @Override
    public void update() {
        chats.removeAllChats();
        for (Chat c : connector.getUsersChats(window.getClient().getUser().getId())) {
            ChatPanel cp = new ChatPanel(this, chats, c);
            cp.setToolTipText(Util.userListToString(c.getMembers()));
            chats.addChat(cp);
            System.out.println("added chat: " + c);
        }
    }
}
