package com.github.jacekpoz.server;

import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.exceptions.UnknownQueryException;
import com.github.jacekpoz.common.exceptions.UnknownSendableException;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.Query;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import com.github.jacekpoz.common.sendables.database.results.ChatResult;
import com.github.jacekpoz.common.sendables.database.results.MessageResult;
import com.github.jacekpoz.common.sendables.database.results.Result;
import com.github.jacekpoz.common.sendables.database.results.UserResult;
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
        if (q instanceof ChatQuery) return handleChatQuery((ChatQuery) q);

        throw new UnknownQueryException(q);
    }

    public MessageResult handleMessageQuery(MessageQuery mq) {
        MessageResult mr = new MessageResult(mq);
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
        UserResult ur = new UserResult(uq);
        switch (uq.getType()) {
            case UserQuery.GET_USER:
                ur.add(connector.getUser(uq.getId()));
                break;
            case UserQuery.GET_USERS_FRIENDS:
                connector.getFriends(connector.getUser(uq.getId()))
                        .forEach(ur::add);
                break;
            case UserQuery.GET_USERS_IN_CHAT:
                connector.getUsersInChat(uq.getId())
                        .forEach(ur::add);
                break;
            default:
                throw new IllegalArgumentException("Invalid UserQuery type: " + uq.getType());
        }
        return ur;
    }

    public ChatResult handleChatQuery(ChatQuery cq) {
        ChatResult cr = new ChatResult(cq);
        cr.add(connector.getChat(cq.getChatID()));
        return cr;
    }
}
