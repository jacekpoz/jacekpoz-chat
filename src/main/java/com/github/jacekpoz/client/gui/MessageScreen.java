package com.github.jacekpoz.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MessageScreen extends JPanel {

    private ChatWindow window;

    public MessageScreen(ChatWindow w) {
        window = w;
        initUI();
    }

    private void initUI() {
        JPanel conversations = new JPanel();

        JScrollPane conversationsScrollPane = new JScrollPane(conversations);
        conversationsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        conversations.setPreferredSize(new Dimension(100, 250));

        JEditorPane messages = new JEditorPane();
        messages.setEditable(false);

        JScrollPane messagesScrollPane = new JScrollPane(messages);
        messagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messagesScrollPane.setPreferredSize(new Dimension(500, 250));

        JTextField field = new JTextField();

        ActionListener sendMessageAction = event -> {
            if (!field.getText().isEmpty()) {
                window.getOutputStream().println(field.getText());
                field.setText("");
                JScrollBar bar = messagesScrollPane.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            }
        };

        field.addActionListener(sendMessageAction);

        JButton button = new JButton("Wyślij");
        button.addActionListener(sendMessageAction);

        createLayout(conversationsScrollPane, messagesScrollPane, field, button);

        new Thread(() -> {
            String fromServer = null;
            while (true) {
                try {
                    if ((fromServer = window.getInputStream().readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                messages.setText(messages.getText() + fromServer + "\n");
            }
        }).start();
    }

    private void createLayout(JComponent... args) {
        GroupLayout gl = new GroupLayout(this);
        setLayout(gl);

        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(args[0])
                .addGroup(gl.createParallelGroup()
                        .addComponent(args[1])
                        .addGroup(gl.createSequentialGroup()
                                .addComponent(args[2])
                                .addComponent(args[3])
                        )
                )
        );

        gl.setVerticalGroup(gl.createParallelGroup()
                .addComponent(args[0])
                .addGroup(gl.createSequentialGroup()
                        .addComponent(args[1])
                        .addGroup(gl.createParallelGroup()
                                .addComponent(args[2])
                                .addComponent(args[3])
                        )
                )
        );
    }
}
