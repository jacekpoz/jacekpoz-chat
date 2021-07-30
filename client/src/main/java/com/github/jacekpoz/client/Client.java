package com.github.jacekpoz.client;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.User;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Client {

    @Getter
    private final Socket socket;
    @Getter
    private final ChatWindow window;
    @Getter @Setter
    private User user;
    @Getter @Setter
    private Chat chat;

    public Client(Socket s) {
        socket = s;
        window = new ChatWindow(this);
        user = new User(-1, "dupa", "dupa dupa", Timestamp.valueOf(LocalDateTime.MIN));
        chat = new Chat(-1, "dupa", Timestamp.valueOf(LocalDateTime.MIN), -1);
    }

    public void start() {
        window.start();
    }

    public void stop() throws IOException {
        socket.close();
        window.dispose();
    }

}
