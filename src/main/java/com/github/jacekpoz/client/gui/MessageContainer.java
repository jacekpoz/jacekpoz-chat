package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.common.Message;
import com.github.jacekpoz.common.Util;

import javax.swing.*;
import java.awt.*;

public class MessageContainer extends JPanel {

    public MessageContainer() {
        BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(bl);
    }

    public void addMessage(Message m) {
        JLabel l = new JLabel(m.getContent());
        l.setToolTipText(Util.timestampToString(m.getSendDate()));
        add(l);
        add(Box.createRigidArea(new Dimension(1, 5)));
        revalidate();
    }

}
