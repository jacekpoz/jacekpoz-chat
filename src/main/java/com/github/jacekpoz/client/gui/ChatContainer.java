package com.github.jacekpoz.client.gui;

import lombok.Getter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class ChatContainer extends JPanel {

    @Getter
    private final List<ChatPanel> chats;

    public ChatContainer() {
        BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(bl);
        chats = new ArrayList<>();
    }

    public void addChat(ChatPanel chat) {
        add(chat);
        chats.add(chat);
        revalidate();
    }

    public void removeAllChats() {
        chats.clear();
        removeAll();
        revalidate();
    }
}
