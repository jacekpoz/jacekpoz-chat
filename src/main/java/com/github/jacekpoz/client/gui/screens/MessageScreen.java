package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatContainer;
import com.github.jacekpoz.client.gui.ChatPanel;
import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.client.gui.MessageContainer;
import com.github.jacekpoz.common.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageScreen extends JPanel {

    private ChatWindow window;
    private DatabaseConnector connector;

    public MessageScreen(ChatWindow w) {
        window = w;
        try {
            connector = new DatabaseConnector(
                    "jdbc:mysql://localhost:3306/" + GlobalStuff.DB_NAME,
                    "chat-client", "DB_Password_0123456789"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initUI();
    }

    private void initUI() {

        JButton friends = new JButton(
                new ImageIcon(
                        new ImageIcon("src/main/resources/friends.png")
                                .getImage()
                                .getScaledInstance(25, 25, Image.SCALE_SMOOTH)
                ));
        friends.addActionListener(a -> window.setScreen(window.getFriendsScreen()));

        ChatContainer chats = new ChatContainer();

        JScrollPane chatsScrollPane = new JScrollPane(chats);
        chatsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chats.setPreferredSize(new Dimension(100, 250));

        MessageContainer messages = new MessageContainer();

        JScrollPane messagesScrollPane = new JScrollPane(messages);
        messagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messagesScrollPane.setPreferredSize(new Dimension(500, 250));

        JTextField messageField = new JTextField();

        ActionListener sendMessageAction = event -> {
            if (!messageField.getText().isEmpty()) {
                Chat c = window.getClient().getChat();
                Message m = new Message(
                        c.getMessageIDs().size(),
                        c.getId(),
                        window.getClient().getUser().getId(),
                        messageField.getText(),
                        Timestamp.valueOf(LocalDateTime.now())
                );
                messages.addMessage(m);
                connector.addMessage(m);
                sendMessage(m);
                messageField.setText("");
                JScrollBar bar = messagesScrollPane.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            }
        };

        messageField.addActionListener(sendMessageAction);

        JButton button = new JButton("Wyślij");
        button.addActionListener(sendMessageAction);

        createLayout(friends, chatsScrollPane, messagesScrollPane, messageField, button);

        ExecutorService service = Executors.newCachedThreadPool();

        service.submit(() -> {
            while (true) {
                try {
                    Message fromServer = (Message) window.getInputStream().readObject();

                    SwingUtilities.invokeLater(() -> messages.addMessage(fromServer));

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

    private void createLayout(JComponent... args) {
        GroupLayout gl = new GroupLayout(this);
        setLayout(gl);

        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(
                gl.createSequentialGroup()
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(args[0])
                                        .addComponent(args[1])
                        )
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(args[2])
                                        .addGroup(
                                                gl.createSequentialGroup()
                                                        .addComponent(args[3])
                                                        .addComponent(args[4])
                                        )
                        )
        );

        gl.setVerticalGroup(
                gl.createParallelGroup()
                        .addGroup(
                                gl.createSequentialGroup()
                                        .addComponent(args[0])
                                        .addComponent(args[1])
                        )
                        .addGroup(
                                gl.createSequentialGroup()
                                        .addComponent(args[2])
                                        .addGroup(
                                                gl.createParallelGroup()
                                                        .addComponent(args[3])
                                                        .addComponent(args[4])
                                        )
                        )
        );
    }

    private void sendMessage(Message message) {
        try {
            System.out.println("sendMessage: " + message);
            window.getOutputStream().writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
