package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.chat.InsertChatQuery;
import com.github.jacekpoz.common.sendables.database.results.UserResult;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CreateChatsScreen implements Screen {

    public static final int ID = 4;

    private transient final ChatWindow window;

    private List<User> friends;

    private transient JPanel createChatsScreen;
    private transient JButton backToMessagesButton;
    private transient JScrollPane friendsScrollPane;
    private transient JScrollPane addedFriendsScrollPane;
    private transient JList<User> friendsList;
    private transient JList<User> addedFriendsList;
    private transient JButton addButton;
    private transient JButton deleteButton;
    private transient JButton createChatButton;
    private transient JTextField chatNameTextField;
    private transient JLabel chatNameLabel;

    private transient final DefaultListModel<User> friendsListModel;
    private transient final DefaultListModel<User> addedFriendsListModel;

    public CreateChatsScreen(ChatWindow w) {
        window = w;

        friends = new ArrayList<>();

        friendsListModel = new DefaultListModel<>();
        friendsList.setModel(friendsListModel);
        addedFriendsListModel = new DefaultListModel<>();
        addedFriendsList.setModel(addedFriendsListModel);

        backToMessagesButton.addActionListener(e -> window.setScreen(window.getMessageScreen()));

        addButton.addActionListener(e -> {
            List<User> selected = friendsList.getSelectedValuesList();
            if (selected.isEmpty()) return;

            for (User u : selected) {
                friendsListModel.removeElement(u);
                addedFriendsListModel.addElement(u);
            }

            createChatButton.setEnabled(true);
            deleteButton.setEnabled(true);
            if (friendsListModel.isEmpty()) addButton.setEnabled(false);
        });

        deleteButton.addActionListener(e -> {
            List<User> selected = addedFriendsList.getSelectedValuesList();
            if (selected.isEmpty()) return;

            for (User u : selected) {
                addedFriendsListModel.removeElement(u);
                friendsListModel.addElement(u);
            }

            addButton.setEnabled(true);
            if (addedFriendsListModel.isEmpty()) {
                deleteButton.setEnabled(false);
                createChatButton.setEnabled(false);
            }
        });

        createChatButton.addActionListener(e -> {
            String inputName = chatNameTextField.getText();
            List<User> users = new ArrayList<>();
            users.add(window.getClient().getUser());
            for (int i = 0; i < addedFriendsListModel.size(); i++)
                users.add(addedFriendsListModel.get(i));

            String chatName = inputName;
            if (inputName.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < users.size(); i++) {
                    sb.append(users.get(i).getNickname());
                    if (i < users.size() - 1)
                        sb.append(", ");
                }
                chatName = sb.toString();
            }

            window.send(new InsertChatQuery(chatName, users, getScreenID()));

            window.setScreen(window.getMessageScreen());
            update();
        });
    }

    @Override
    public JPanel getPanel() {
        return createChatsScreen;
    }

    @Override
    public void update() {
        friendsListModel.clear();
        addedFriendsListModel.clear();
        chatNameTextField.setText("");
        for (User u : friends)
            friendsListModel.addElement(u);
        window.getMessageScreen().update();
    }

    @Override
    public void handleSendable(Sendable s) {
        if (s instanceof UserResult) {
            UserResult ur = (UserResult) s;
            friends = ur.get();
        }

        update();
    }

    @Override
    public long getScreenID() {
        return ID;
    }
}
