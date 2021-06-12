package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.client.gui.UserPanel;
import com.github.jacekpoz.common.DatabaseConnector;
import com.github.jacekpoz.common.UserInfo;
import com.github.jacekpoz.common.Util;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class FriendsScreen extends JPanel {

    private ChatWindow window;
    private DatabaseConnector connector;

    public FriendsScreen(ChatWindow w) {
        window = w;
        try {
            connector = new DatabaseConnector(
                    "jdbc:mysql://localhost:3306/mydatabase",
                    "chat-client", "DB_Password_0123456789"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initUI();
    }

    private void initUI() {
        JTabbedPane pane = new JTabbedPane();

        JPanel yourFriends = new JPanel();

        JTextPane searchYourFriends = new JTextPane();

        JButton searchYourFriendsButton = new JButton("Szukaj");

        JPanel yourFriendsList = new JPanel();

        searchYourFriendsButton.addActionListener(a -> {
            String username = searchYourFriends.getText();
            List<UserInfo> similarUsers = Util.compareUsernames(username, window.getClient().getUser().getFriends());

            yourFriendsList.removeAll();

            addFriends(yourFriendsList, similarUsers, UserPanel.FRIEND);
        });

        createTab(yourFriends, searchYourFriends, searchYourFriendsButton, yourFriendsList);

        JPanel searchFriends = new JPanel();

        JTextPane searchNewFriends = new JTextPane();

        JButton searchNewFriendsButton = new JButton("Szukaj");

        JPanel searchedFriendsList = new JPanel();

        searchNewFriendsButton.addActionListener(a -> {
            String username = searchNewFriends.getText();
            List<UserInfo> similarUsers = Util.compareUsernames(username, connector.getAllUsers());

            searchedFriendsList.removeAll();

            addFriends(searchedFriendsList, similarUsers, UserPanel.NOT_FRIEND);
        });

        createTab(searchFriends, searchNewFriends, searchNewFriendsButton, searchedFriendsList);

        pane.addTab("Znajomi", yourFriends);

        pane.addTab("Dodaj znajomych", searchFriends);


        add(pane);
    }

    private void createTab(JPanel p, JComponent... args) {
        GroupLayout gl = new GroupLayout(p);
        p.setLayout(gl);

        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(
                gl.createParallelGroup()
                        .addGroup(
                                gl.createSequentialGroup()
                                        .addComponent(args[0])
                                        .addComponent(args[1])
                        )
                        .addComponent(args[2])
        );

        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(args[0])
                                        .addComponent(args[1])
                        )
                        .addComponent(args[2])
        );
    }

    private void addFriends(JPanel p, List<UserInfo> users, int type) {
        users.forEach(user -> p.add(new UserPanel(connector, window.getClient().getUser(), user, type)));
        p.revalidate();
    }
}
