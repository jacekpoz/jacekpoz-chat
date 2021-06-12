package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.client.gui.MessageContainer;
import com.github.jacekpoz.common.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageScreen extends JPanel {

    private ChatWindow window;

    public MessageScreen(ChatWindow w) {
        window = w;
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

        JPanel conversations = new JPanel();

        JScrollPane conversationsScrollPane = new JScrollPane(conversations);
        conversationsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        conversations.setPreferredSize(new Dimension(100, 250));

        MessageContainer messages = new MessageContainer();

        JScrollPane messagesScrollPane = new JScrollPane(messages);
        messagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messagesScrollPane.setPreferredSize(new Dimension(500, 250));

        JTextField messageField = new JTextField();

        ActionListener sendMessageAction = event -> {
            if (!messageField.getText().isEmpty()) {
                Message m = new Message(messageField.getText(), window.getClient().getUser());
                messages.addMessage(m);
                sendMessage(m);
                messageField.setText("");
                JScrollBar bar = messagesScrollPane.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            }
        };

        messageField.addActionListener(sendMessageAction);

        JButton button = new JButton("Wyślij");
        button.addActionListener(sendMessageAction);

        createLayout(friends, conversationsScrollPane, messagesScrollPane, messageField, button);

        ExecutorService service = Executors.newCachedThreadPool();

        service.submit(() -> {
            while (true) {
                try {
                    Message fromServer = (Message) window.getInputStream().readObject();

                    if (fromServer.getLabel() == null) fromServer.initLabel();

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
