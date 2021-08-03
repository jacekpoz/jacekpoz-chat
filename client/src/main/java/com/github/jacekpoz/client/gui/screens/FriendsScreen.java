package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.client.gui.UserPanel;
import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.Util;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.user.GetAllUsersQuery;
import com.github.jacekpoz.common.sendables.database.queries.user.GetFriendRequestsQuery;
import com.github.jacekpoz.common.sendables.database.queries.user.GetFriendsQuery;
import com.github.jacekpoz.common.sendables.database.results.UserResult;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FriendsScreen implements Screen {

    public static final int ID = 3;

    private transient final ChatWindow window;

    private transient JPanel friendsScreen;
    private transient JTabbedPane pane;
    private transient JPanel friendsPane;
    private transient JPanel addFriendsPane;
    private transient JPanel friendRequestsPane;
    private transient JTextField searchFriends;
    private transient JButton searchFriendsButton;
    private transient JTextField searchNewFriends;
    private transient JButton searchNewFriendsButton;
    private transient JPanel newFriendsList;
    private transient JPanel friendsList;
    private transient JScrollPane friendsScrollPane;
    private transient JScrollPane newFriendsScrollPane;
    private transient JButton backToMessages;

    private List<User> friends;
    private List<User> friendRequests;
    private List<User> allUsers;

    public FriendsScreen(ChatWindow w) {
        window = w;
        friends = new ArrayList<>();
        friendRequests = new ArrayList<>();
        allUsers = new ArrayList<>();

        ActionListener searchFriendsAction = e -> {
            String username = searchFriends.getText();
            if (username.isEmpty()) {
                addUsersToPanel(friendsList, friends, UserPanel.FRIEND);
                return;
            }
            List<User> similarUsers = Util.compareUsernames(username, friends);
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
            List<User> similarUsers = Util.compareUsernames(username, allUsers);

            newFriendsList.removeAll();

            addUsersToPanel(newFriendsList, similarUsers, UserPanel.NOT_FRIEND);
        };

        searchFriends.addActionListener(searchNewFriendsAction);
        searchNewFriendsButton.addActionListener(searchNewFriendsAction);

        backToMessages.addActionListener(e -> window.setScreen(window.getMessageScreen()));

        friendRequestsPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addUsersToPanel(friendRequestsPane, friendRequests, UserPanel.REQUEST);
            }
        });
    }

    private void addUsersToPanel(JPanel p, List<User> users, int type) {
        p.removeAll();
        for (User u : users)
            if (window.getClient().getUser().getId() != u.getId())
                p.add(new UserPanel(window, window.getClient().getUser(), u, type));
        p.revalidate();
    }

    @Override
    public JPanel getPanel() {
        return friendsScreen;
    }

    @Override
    public void update() {
        window.send(new GetFriendsQuery(window.getClient().getUser(), getScreenID()));
        window.send(new GetFriendRequestsQuery(window.getClient().getUser(), getScreenID()));
        window.send(new GetAllUsersQuery(getScreenID()));
        friendsList.removeAll();
        friendRequestsPane.removeAll();
        addUsersToPanel(friendsList, friends, UserPanel.FRIEND);
        addUsersToPanel(friendRequestsPane, friendRequests, UserPanel.REQUEST);
    }

    @Override
    public void handleSendable(Sendable s) {
        if (s instanceof UserResult) {
            UserResult ur = (UserResult) s;
            if (ur.getQuery() instanceof GetFriendsQuery) friends = ur.get();
            else if (ur.getQuery() instanceof GetFriendRequestsQuery) friendRequests = ur.get();
            else if (ur.getQuery() instanceof GetAllUsersQuery) allUsers = ur.get();
        }
    }

    @Override
    public long getScreenID() {
        return ID;
    }
}
