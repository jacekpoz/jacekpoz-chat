package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.DatabaseConnector;
import com.github.jacekpoz.common.User;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateChatsScreen implements Screen {
    private final ChatWindow window;
    private DatabaseConnector connector;

    private JPanel createChatsScreen;
    private JButton backToMessagesButton;
    private JScrollPane friendsScrollPane;
    private JScrollPane addedFriendsScrollPane;
    private JList<User> friendsList;
    private JList<User> addedFriendsList;
    private JButton addButton;
    private JButton deleteButton;
    private JButton createChatButton;
    private JTextField chatNameTextField;
    private JLabel chatNameLabel;

    private final DefaultListModel<User> friendsListModel;
    private final DefaultListModel<User> addedFriendsListModel;

    public CreateChatsScreen(ChatWindow w) {
        window = w;
        try {
            connector = new DatabaseConnector(
                    "jdbc:mysql://localhost:3306/" + Constants.DB_NAME,
                    "chat-client", "DB_Password_0123456789"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

            connector.createChat(chatName, users);
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
        for (User u : connector.getFriends(window.getClient().getUser()))
            friendsListModel.addElement(u);
        window.getMessageScreen().update();
    }
}
