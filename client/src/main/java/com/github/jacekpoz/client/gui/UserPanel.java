package com.github.jacekpoz.client.gui;

import com.github.jacekpoz.common.DatabaseConnector;
import com.github.jacekpoz.common.sendables.User;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {

    public static final int NOT_FRIEND = 0;
    public static final int FRIEND = 1;
    public static final int REQUEST = 2;

    private final DatabaseConnector connector;
    @Getter
    private final User clientUser;
    @Getter
    private final User panelUser;
    @Getter @Setter
    private int userPanelType;
    private JButton button1;
    private JButton button2;

    public UserPanel(DatabaseConnector con, User u, User pU, int type) {
        connector = con;
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
                button1 = new JButton(new ImageIcon(
                        new ImageIcon("src/main/resources/addFriend.png")
                                .getImage()
                                .getScaledInstance(25, 25, Image.SCALE_SMOOTH)
                ));
                button1.addActionListener(a -> {
                    System.out.println(connector.sendFriendRequest(clientUser, panelUser));
                });
                break;
            case FRIEND:
                button1 = new JButton(new ImageIcon(
                        new ImageIcon("src/main/resources/deleteFriend.png")
                                .getImage()
                                .getScaledInstance(25, 25, Image.SCALE_SMOOTH)
                ));
                button1.addActionListener(a -> {
                    clientUser.removeFriend(panelUser);
                    System.out.println(connector.removeFriend(clientUser, panelUser));
                });
                break;
            case REQUEST:
                button1 = new JButton("A");
                button1.addActionListener(a -> {
                    connector.acceptFriendRequest(panelUser, clientUser);
                    clientUser.addFriend(panelUser);
                    System.out.println("Friend request accepted");
                });
                button2 = new JButton("D");
                button2.addActionListener(a -> {
                    connector.denyFriendRequest(panelUser, clientUser);
                    remove(button1);
                    remove(button2);
                    System.out.println("Friend request denied");
                });
                break;
            default:
                throw new IllegalArgumentException("You have to pass in NOT_FRIEND, FRIEND or REQUEST");
        }

        if (button1 != null) add(button1);
        if (button2 != null) add(button2);

        revalidate();
    }
}
