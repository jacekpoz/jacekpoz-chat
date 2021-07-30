package com.github.jacekpoz.common;

import com.github.jacekpoz.common.sendables.Sendable;

import javax.swing.*;

public interface Screen {

    JPanel getPanel();

    void update();

    void handleSendable(Sendable s);

}
