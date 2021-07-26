package com.github.jacekpoz.server;

import com.github.jacekpoz.common.*;
import com.github.jacekpoz.common.database.queries.MessageQuery;
import com.github.jacekpoz.common.database.queries.Query;
import com.github.jacekpoz.common.exceptions.UnknownQueryException;
import com.github.jacekpoz.common.database.queries.UserQuery;
import com.github.jacekpoz.common.database.results.MessageResult;
import com.github.jacekpoz.common.database.results.Result;
import com.github.jacekpoz.common.database.results.UserResult;
import com.github.jacekpoz.common.exceptions.UnknownSendableException;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.SQLException;

public class InputHandler {

    private final ChatWorker worker;
    private DatabaseConnector connector;
    private final Gson gson;

    public InputHandler(ChatWorker w) {
        worker = w;
        try {
            connector = new DatabaseConnector("jdbc:mysql://localhost:3306/" + Constants.DB_NAME,
                    "chat-client", System.getenv("DB_PASSWORD"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gson = new Gson();
    }

    public void handleInput(Sendable input) throws IOException {
        System.out.println("input: " + input);

        if (input instanceof User) handleUser((User) input);
        if (input instanceof Chat) handleChat((Chat) input);
        if (input instanceof Message) handleMessage((Message) input);

        throw new UnknownSendableException(input);
    }

    private void handleUser(User u) {
        worker.setCurrentUser(u);
    }

    private void handleChat(Chat c) {
        worker.setCurrentChat(c);
    }

    private void handleMessage(Message m) {
        for (ChatWorker ct : worker.getServer().getThreads())
            if (worker.getCurrentChat().getMembers().contains(ct.getCurrentUser()))
                ct.send(gson.toJson(m, m.getClass()));
    }

    public Result handleQuery(Query q) {
        if (q instanceof MessageQuery) return handleMessageQuery((MessageQuery) q);
        if (q instanceof UserQuery) return handleUserQuery((UserQuery) q);

        throw new UnknownQueryException(q);
    }

    public MessageResult handleMessageQuery(MessageQuery mq) {
        MessageResult mr = new MessageResult();
        if (mq.getAuthorID().isPresent()) {
            connector.getMessagesFromChat(
                    mq.getChatID(), mq.getOffset(), mq.getMessageLimit(), mq.getAuthorID().get()
            ).forEach(mr::add);
        } else {
            connector.getMessagesFromChat(
                    mq.getChatID(), mq.getOffset(), mq.getMessageLimit()
            ).forEach(mr::add);
        }
        return mr;
    }

    public UserResult handleUserQuery(UserQuery uq) {
        UserResult ur = new UserResult();
        switch (uq.getType()) {
            case UserQuery.SINGLE_USER:
                ur.add(connector.getUser(uq.getId()));
                break;
            case UserQuery.USERS_FRIENDS:
                connector.getFriends(connector.getUser(uq.getId()))
                        .forEach(ur::add);
                break;
            case UserQuery.USERS_IN_CHAT:
                connector.getUsersInChat(uq.getId())
                        .forEach(ur::add);
                break;
            default:
                throw new IllegalArgumentException("Invalid UserQuery type: " + uq.getType());
        }
        return ur;
    }
}
