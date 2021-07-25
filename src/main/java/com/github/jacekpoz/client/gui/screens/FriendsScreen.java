package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.client.gui.UserPanel;
import com.github.jacekpoz.common.DatabaseConnector;
import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.User;
import com.github.jacekpoz.common.Util;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class FriendsScreen implements Screen {
    private final ChatWindow window;
    private DatabaseConnector connector;

    private JPanel friendsScreen;
    private JTabbedPane pane;
    private JPanel friendsPane;
    private JPanel addFriendsPane;
    private JPanel friendRequestsPane;
    private JTextField searchFriends;
    private JButton searchFriendsButton;
    private JTextField searchNewFriends;
    private JButton searchNewFriendsButton;
    private JPanel newFriendsList;
    private JPanel friendsList;
    private JScrollPane friendsScrollPane;
    private JScrollPane newFriendsScrollPane;
    private JButton backToMessages;

    public FriendsScreen(ChatWindow w) {
        window = w;
        try {
            connector = new DatabaseConnector(
                    "jdbc:mysql://localhost:3306/" + Constants.DB_NAME,
                    "chat-client", "DB_Password_0123456789"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ActionListener searchFriendsAction = e -> {
            String username = searchFriends.getText();
            if (username.isEmpty()) {
                addUsersToPanel(friendsList, connector.getFriends(window.getClient().getUser()), UserPanel.FRIEND);
                return;
            }
            List<User> similarUsers = Util.compareUsernamesFromID(username, window.getClient().getUser().getFriendsIds());
            if (similarUsers.isEmpty()) {
                return;
            }

            friendsList.removeAll();

            addUsersToPanel(friendsList, similarUsers, UserPanel.FRIEND);
        };

        searchFriends.addActionListener(searchFriendsAction);
        searchFriendsButton.addActionListener(searchFriendsAction);

        ActionListener searchNewFriendsAction = e -> {
            String username = searchNewFriends.getText();
            if (username.isEmpty()) return;
            List<User> similarUsers = Util.compareUsernames(username, connector.getAllUsers());

            newFriendsList.removeAll();

            addUsersToPanel(newFriendsList, similarUsers, UserPanel.NOT_FRIEND);
        };

        searchFriends.addActionListener(searchNewFriendsAction);
        searchNewFriendsButton.addActionListener(searchNewFriendsAction);

        backToMessages.addActionListener(e -> window.setScreen(window.getMessageScreen()));

        friendRequestsPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addUsersToPanel(friendRequestsPane, connector.getFriendRequests(window.getClient().getUser()), UserPanel.REQUEST);
            }
        });
    }

    private void addUsersToPanel(JPanel p, List<User> users, int type) {
        p.removeAll();
        for (User u : users)
            if (window.getClient().getUser().getId() != u.getId())
                p.add(new UserPanel(connector, window.getClient().getUser(), u, type));
        p.revalidate();
    }

    @Override
    public JPanel getPanel() {
        return friendsScreen;
    }

    @Override
    public void update() {
        friendsList.removeAll();
        friendRequestsPane.removeAll();
        addUsersToPanel(friendsList, connector.getFriends(window.getClient().getUser()), UserPanel.FRIEND);
        addUsersToPanel(friendRequestsPane, connector.getFriendRequests(window.getClient().getUser()), UserPanel.REQUEST);
    }
}
