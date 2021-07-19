package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.common.Chat;
import com.github.jacekpoz.common.DatabaseConnector;
import com.github.jacekpoz.common.UserInfo;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ChatPanel extends JPanel {

    public ChatPanel(ChatWindow w, Chat c) {
        add(new JLabel("dupa " + c.getId()));
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                w.getClient().setChat(c);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

}
