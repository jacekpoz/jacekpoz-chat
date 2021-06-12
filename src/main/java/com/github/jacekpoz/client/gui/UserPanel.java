package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.common.DatabaseConnector;
import com.github.jacekpoz.common.FriendException;
import com.github.jacekpoz.common.UserInfo;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserPanel extends JPanel {

    public static final int NOT_FRIEND = 0;
    public static final int FRIEND = 1;

    private final DatabaseConnector connector;
    private final @Getter UserInfo user;
    private final @Getter UserInfo userToAdd;
    private @Getter @Setter int userPanelType;
    private JButton button;

    public UserPanel(DatabaseConnector con, UserInfo u, UserInfo userAdd, int type) {
        connector = con;
        user = u;
        userToAdd = userAdd;
        userPanelType = type;
        add(new JLabel(userToAdd.getNickname() + "(ID=" + userToAdd.getId() + ")"));
        try {
            changeType(userPanelType);
        } catch (FriendException e) {
            e.printStackTrace();
        }
    }

    public void changeType(int type) throws FriendException {

        if (type == NOT_FRIEND) {
            button = new JButton(new ImageIcon(
                    new ImageIcon("src/main/resources/addFriend.png")
                            .getImage()
                            .getScaledInstance(25, 25, Image.SCALE_SMOOTH)
            ));
            button.addActionListener(a -> {

            });
        } else if (type == FRIEND) {
            button = new JButton(new ImageIcon(
                    new ImageIcon("src/main/resources/deleteFriend.png")
                            .getImage()
                            .getScaledInstance(25, 25, Image.SCALE_SMOOTH)
            ));
            button.addActionListener(a -> {

            });
        }

        add(button);
        revalidate();
    }
}
