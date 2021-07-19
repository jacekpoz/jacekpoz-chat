package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.common.DatabaseConnector;
import com.github.jacekpoz.common.UserInfo;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {

    public static final int NOT_FRIEND = 0;
    public static final int FRIEND = 1;

    private final DatabaseConnector connector;
    private final @Getter UserInfo clientUser;
    private final @Getter UserInfo panelUser;
    private @Getter @Setter int userPanelType;
    private JButton button;

    public UserPanel(DatabaseConnector con, UserInfo u, UserInfo userAdd, int type) {
        connector = con;
        clientUser = u;
        panelUser = userAdd;
        userPanelType = type;
        add(new JLabel(panelUser.getNickname() + "(ID=" + panelUser.getId() + ")"));
        changeType(userPanelType);
    }

    public void changeType(int type) {
        if (type == NOT_FRIEND) {
            button = new JButton(new ImageIcon(
                    new ImageIcon("src/main/resources/addFriend.png")
                            .getImage()
                            .getScaledInstance(25, 25, Image.SCALE_SMOOTH)
            ));
            button.addActionListener(a -> {
                clientUser.addFriend(panelUser);
                System.out.println(connector.addFriend(clientUser, panelUser));
            });
        } else if (type == FRIEND) {
            button = new JButton(new ImageIcon(
                    new ImageIcon("src/main/resources/deleteFriend.png")
                            .getImage()
                            .getScaledInstance(25, 25, Image.SCALE_SMOOTH)
            ));
            button.addActionListener(a -> {
                clientUser.removeFriend(panelUser);
                connector.removeFriend(clientUser, panelUser);
            });
        }

        add(button);
        revalidate();
    }
}
