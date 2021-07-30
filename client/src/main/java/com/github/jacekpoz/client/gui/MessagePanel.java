package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.Util;

import javax.swing.*;
import java.awt.*;

public class MessagePanel extends JPanel {

    private final JLabel label;

    public MessagePanel(User author, Message m) {
        label = new JLabel(author.getNickname() + ": " + m.getContent());
        label.setToolTipText(Util.timestampToString(m.getSendDate()));
        label.setForeground(Color.WHITE);
        setBackground(new Color(60, 60, 60));
        add(label);
    }

}