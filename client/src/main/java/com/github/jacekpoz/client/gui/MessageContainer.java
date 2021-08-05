package com.github.jacekpoz.client.gui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class MessageContainer extends JPanel {

    private final ChatWindow window;

    @Getter
    private final List<MessagePanel> messages;

    @Getter
    private final JLabel noMessages;

    public MessageContainer(ChatWindow w) {
        window = w;
        BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(bl);
        messages = new ArrayList<>();
        noMessages = new JLabel(window.getLanguageBundle().getString("app.no_messages_in_chat"));
        noMessages.setForeground(Color.WHITE);
    }

    public void addMessage(MessagePanel mp) {
        add(mp);
        messages.add(mp);
        remove(noMessages);
        revalidate();
    }

    public void removeAllMessages() {
        messages.clear();
        removeAll();
        revalidate();
    }

}
