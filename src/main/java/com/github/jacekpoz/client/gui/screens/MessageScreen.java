package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.*;
import com.github.jacekpoz.common.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageScreen implements Screen {
    private final ChatWindow window;
    private DatabaseConnector connector;

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
        try {
            connector = new DatabaseConnector(
                    "jdbc:mysql://localhost:3306/" + Constants.DB_NAME,
                    "chat-client", "DB_Password_0123456789"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
                connector.addMessage(m);
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
                    Message fromServer = (Message) window.getInputStream().readObject();

                    SwingUtilities.invokeLater(() -> messages.addMessage(
                            new MessagePanel(connector.getUser(fromServer.getAuthorID()), fromServer)
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
        try {
            System.out.println("sendMessage: " + message);
            window.getOutputStream().writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setChat(Chat c) {
        try {
            window.getClient().setChat(c);
            window.getOutputStream().writeObject(c);
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
