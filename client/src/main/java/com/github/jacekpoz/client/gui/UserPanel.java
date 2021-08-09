package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.user.AcceptFriendRequestQuery;
import com.github.jacekpoz.common.sendables.database.queries.user.DenyFriendRequestQuery;
import com.github.jacekpoz.common.sendables.database.queries.user.RemoveFriendQuery;
import com.github.jacekpoz.common.sendables.database.queries.user.SendFriendRequestQuery;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserPanel extends JPanel {

    private final static Logger LOGGER = Logger.getLogger(UserPanel.class.getName());

    public static final int NOT_FRIEND = 0;
    public static final int FRIEND = 1;
    public static final int REQUEST = 2;

    private final ChatWindow window;
    @Getter
    private final User clientUser;
    @Getter
    private final User panelUser;
    @Getter @Setter
    private int userPanelType;
    private JButton button1;
    private JButton button2;

    public UserPanel(ChatWindow w, User u, User pU, int type) {
        window = w;
        clientUser = u;
        panelUser = pU;
        userPanelType = type;
        add(new JLabel(String.valueOf(panelUser)));
        changeType(userPanelType);
    }

    public void changeType(int type) {
        for (Component c : getComponents())
            if (c instanceof JButton)
                remove(c);

        button1 = null;
        button2 = null;

        switch (type) {
            case NOT_FRIEND:
                button1 = new JButton(new ImageIcon("images/add_friend.png"));
                button1.addActionListener(a -> {
                    window.send(new SendFriendRequestQuery(
                            clientUser.getId(),
                            panelUser.getId(),
                            window.getFriendsScreen().getScreenID())
                    );
                    LOGGER.log(Level.INFO, "Sent friend request", panelUser);
                    removeThis();
                });
                break;
            case FRIEND:
                button1 = new JButton(new ImageIcon("images/delete_friend.png"));
                button1.addActionListener(a -> {
                    clientUser.removeFriend(panelUser);
                    window.send(new RemoveFriendQuery(
                            clientUser.getId(),
                            panelUser.getId(),
                            window.getFriendsScreen().getScreenID())
                    );
                    LOGGER.log(Level.INFO, "Removed friend", panelUser);
                    removeThis();
                });
                break;
            case REQUEST:
                button1 = new JButton(new ImageIcon("images/add_friend.png"));
                button1.addActionListener(a -> {
                    clientUser.addFriend(panelUser);
                    window.send(new AcceptFriendRequestQuery(
                            panelUser.getId(),
                            clientUser.getId(),
                            window.getFriendsScreen().getScreenID()
                    ));
                    LOGGER.log(Level.INFO, "Accepted friend request", panelUser);
                    removeThis();
                });
                button2 = new JButton(new ImageIcon("images/delete_friend.png"));
                button2.addActionListener(a -> {
                    window.send(new DenyFriendRequestQuery(
                            panelUser.getId(),
                            clientUser.getId(),
                            window.getFriendsScreen().getScreenID()
                    ));
                    LOGGER.log(Level.INFO, "Denied friend request", panelUser);
                    removeThis();
                });
                break;
            default:
                throw new IllegalArgumentException("You have to pass in NOT_FRIEND, FRIEND or REQUEST");
        }

        if (button1 != null) {
            button1.setBackground(new Color(60, 60, 60));
            button1.setForeground(Color.WHITE);
            button1.setBorderPainted(false);
            add(button1);
        }
        if (button2 != null) {
            button2.setBackground(new Color(60, 60, 60));
            button2.setForeground(Color.WHITE);
            button2.setBorderPainted(false);
            add(button2);
        }

        revalidate();
    }

    private void removeThis() {
        window.getFriendsScreen().getPanel().remove(this);
        window.getFriendsScreen().getPanel().revalidate();
    }
}
