package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.common.Message;

import javax.swing.*;
import java.awt.*;

public class MessageContainer extends JPanel {

    public MessageContainer() {
        BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(bl);
    }

    public synchronized void addMessage(Message message) {
        add(message.getLabel());
        add(Box.createRigidArea(new Dimension(1, 5)));
        revalidate();
    }

}
