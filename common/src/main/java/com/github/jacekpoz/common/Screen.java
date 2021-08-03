package com.github.jacekpoz.common;

import com.github.jacekpoz.common.sendables.Sendable;

import javax.swing.*;

/**
 * Base interface implemented by all screens in the desktop client.
 *
 * @author  jacekpoz
 * @version 0.0.2
 * @since   0.0.1
 */

public interface Screen {

    /**
     * Getter for the main JPanel.
     *
     * @return main JPanel containing everything in the current screen
     * @author jacekpoz
     * @since  0.0.1
     */
    JPanel getPanel();

    /**
     * Method called when the GUI needs to be synced with the database.
     * @author jacekpoz
     * @since  0.0.1
     */
    void update();

    /**
     * Method called when a screen needs to handle a certain Sendable.
     *
     * @param  s Sendable to handle
     * @author jacekpoz
     * @since  0.0.2
     */
    void handleSendable(Sendable s);

    /**
     * ID is used to identify the screens instead of passing them to methods.
     *
     * @return screen ID
     * @author jacekpoz
     * @since  0.0.3
     */
    long getScreenID();

}
