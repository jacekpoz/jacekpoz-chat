package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.client.gui.screens.MessageScreen;
import com.github.jacekpoz.common.sendables.Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChatPanel extends JPanel {

    private final JLabel label;

    public ChatPanel(MessageScreen m, ChatContainer parent, Chat c) {
        label = new JLabel(c.getName());
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
        add(label);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m.setChat(c);
                for (ChatPanel cp : parent.getChats())
                    cp.deselectChat();
                selectChat();
            }
        });
    }

    public void selectChat() {
        label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2, true));
    }

    public void deselectChat() {
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
    }

}
