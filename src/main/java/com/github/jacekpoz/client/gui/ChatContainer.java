package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.common.Chat;

import javax.swing.*;
import java.awt.*;

public class ChatContainer extends JPanel {

    public ChatContainer() {
        BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(bl);
    }

    public synchronized void addChat(ChatPanel chat) {
        add(chat);
        add(Box.createRigidArea(new Dimension(1, 5)));
        revalidate();
    }
}
