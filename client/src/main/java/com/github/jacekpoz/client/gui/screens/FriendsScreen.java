package com.github.jacekpoz.client.gui.screens;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.client.gui.UserPanel;
import com.github.jacekpoz.client.gui.Screen;
import com.github.jacekpoz.common.Util;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.user.GetAllUsersQuery;
import com.github.jacekpoz.common.sendables.database.queries.user.GetFriendRequestsQuery;
import com.github.jacekpoz.common.sendables.database.queries.user.GetFriendsQuery;
import com.github.jacekpoz.common.sendables.database.results.UserResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FriendsScreen implements Screen {

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
    private transient JButton backToMessagesButton;

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
            update();

            List<User> similarUsers = Util.compareUsernames(username, friends);
            if (similarUsers.isEmpty()) return;

            addUsersToPanel(friendsList, similarUsers, UserPanel.FRIEND);
        };

        searchFriends.addActionListener(searchFriendsAction);
        searchFriendsButton.addActionListener(searchFriendsAction);

        ActionListener searchNewFriendsAction = e -> {
            String username = searchNewFriends.getText();
            if (username.isEmpty()) return;
            update();

            List<User> similarUsers = Util.compareUsernames(username, allUsers);
            System.out.println(similarUsers);

            addUsersToPanel(newFriendsList, similarUsers, UserPanel.NOT_FRIEND);
        };

        searchFriends.addActionListener(searchNewFriendsAction);
        searchNewFriendsButton.addActionListener(searchNewFriendsAction);

        backToMessagesButton.addActionListener(e -> window.setScreen(window.getMessageScreen()));

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
        window.send(new GetFriendsQuery(window.getClient().getUser().getId(), getScreenID()));
        window.send(new GetFriendRequestsQuery(window.getClient().getUser().getId(), getScreenID()));
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
            if (ur.getQuery() instanceof GetFriendsQuery) {
                friends = ur.get();
                System.out.println(friends);
                addUsersToPanel(friendsList, friends, UserPanel.FRIEND);
            } else if (ur.getQuery() instanceof GetFriendRequestsQuery) {
                friendRequests = ur.get();
                System.out.println(friendRequests);
                addUsersToPanel(friendRequestsPane, friendRequests, UserPanel.REQUEST);
            } else if (ur.getQuery() instanceof GetAllUsersQuery) {
                allUsers = ur.get();
                System.out.println(allUsers);
            }
        }
    }

    @Override
    public long getScreenID() {
        return 3;
    }

    @Override
    public void changeLanguage() {
        ResourceBundle lang = window.getLanguageBundle();
        backToMessagesButton.setText(lang.getString("go_back"));
        pane.setTitleAt(0, lang.getString("friends"));
        pane.setTitleAt(1, lang.getString("add_friends"));
        pane.setTitleAt(2, lang.getString("friend_requests"));
        searchFriendsButton.setText(lang.getString("search"));
        searchNewFriendsButton.setText(lang.getString("search"));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        friendsScreen = new JPanel();
        friendsScreen.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        friendsScreen.setBackground(new Color(-12829636));
        friendsScreen.setForeground(new Color(-1));
        pane = new JTabbedPane();
        pane.setBackground(new Color(-12829636));
        pane.setForeground(new Color(-1));
        friendsScreen.add(pane, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        friendsPane = new JPanel();
        friendsPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        friendsPane.setBackground(new Color(-12829636));
        friendsPane.setForeground(new Color(-1));
        pane.addTab(this.$$$getMessageFromBundle$$$("lang", "friends"), friendsPane);
        searchFriends = new JTextField();
        searchFriends.setBackground(new Color(-12829636));
        searchFriends.setForeground(new Color(-1));
        friendsPane.add(searchFriends, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchFriendsButton = new JButton();
        searchFriendsButton.setBackground(new Color(-12829636));
        searchFriendsButton.setForeground(new Color(-1));
        this.$$$loadButtonText$$$(searchFriendsButton, this.$$$getMessageFromBundle$$$("lang", "search"));
        friendsPane.add(searchFriendsButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        friendsScrollPane = new JScrollPane();
        friendsScrollPane.setBackground(new Color(-12829636));
        friendsScrollPane.setForeground(new Color(-1));
        friendsScrollPane.setHorizontalScrollBarPolicy(31);
        friendsPane.add(friendsScrollPane, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        friendsList = new JPanel();
        friendsList.setLayout(new GridBagLayout());
        friendsList.setBackground(new Color(-12829636));
        friendsList.setForeground(new Color(-1));
        friendsScrollPane.setViewportView(friendsList);
        addFriendsPane = new JPanel();
        addFriendsPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        addFriendsPane.setBackground(new Color(-12829636));
        addFriendsPane.setForeground(new Color(-1));
        pane.addTab(this.$$$getMessageFromBundle$$$("lang", "add_friends"), addFriendsPane);
        searchNewFriends = new JTextField();
        searchNewFriends.setBackground(new Color(-12829636));
        searchNewFriends.setForeground(new Color(-1));
        addFriendsPane.add(searchNewFriends, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchNewFriendsButton = new JButton();
        searchNewFriendsButton.setBackground(new Color(-12829636));
        searchNewFriendsButton.setForeground(new Color(-1));
        searchNewFriendsButton.setText("Szukaj");
        addFriendsPane.add(searchNewFriendsButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        newFriendsScrollPane = new JScrollPane();
        newFriendsScrollPane.setBackground(new Color(-12829636));
        newFriendsScrollPane.setForeground(new Color(-1));
        newFriendsScrollPane.setHorizontalScrollBarPolicy(31);
        addFriendsPane.add(newFriendsScrollPane, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        newFriendsList = new JPanel();
        newFriendsList.setLayout(new GridBagLayout());
        newFriendsList.setBackground(new Color(-12829636));
        newFriendsList.setForeground(new Color(-1));
        newFriendsScrollPane.setViewportView(newFriendsList);
        friendRequestsPane = new JPanel();
        friendRequestsPane.setLayout(new GridBagLayout());
        friendRequestsPane.setBackground(new Color(-12829636));
        friendRequestsPane.setForeground(new Color(-1));
        pane.addTab(this.$$$getMessageFromBundle$$$("lang", "friend_requests"), friendRequestsPane);
        backToMessagesButton = new JButton();
        backToMessagesButton.setBackground(new Color(-12829636));
        backToMessagesButton.setForeground(new Color(-1));
        this.$$$loadButtonText$$$(backToMessagesButton, this.$$$getMessageFromBundle$$$("lang", "go_back"));
        friendsScreen.add(backToMessagesButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return friendsScreen;
    }

}
