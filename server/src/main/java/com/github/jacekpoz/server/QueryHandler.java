package com.github.jacekpoz.server;

import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.exceptions.UnknownQueryException;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.database.EnumResults;
import com.github.jacekpoz.common.sendables.database.queries.chat.GetChatQuery;
import com.github.jacekpoz.common.sendables.database.queries.chat.GetUsersChatsQuery;
import com.github.jacekpoz.common.sendables.database.queries.chat.InsertChatQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.*;
import com.github.jacekpoz.common.sendables.database.queries.message.GetMessagesInChatQuery;
import com.github.jacekpoz.common.sendables.database.queries.message.InsertMessageQuery;
import com.github.jacekpoz.common.sendables.database.queries.user.*;
import com.github.jacekpoz.common.sendables.database.results.*;

import java.sql.SQLException;
import java.util.List;

public class QueryHandler {

    private DatabaseConnector connector;

    public QueryHandler() {
        try {
            connector = new DatabaseConnector("jdbc:mysql://localhost:3306/" + Constants.DB_NAME,
                    "chat-client", System.getenv("DB_PASSWORD"));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Result<?> handleQuery(Query<?> q) {
        if (q instanceof MessageQuery mq) return handleMessageQuery(mq);
        else if (q instanceof ChatQuery cq) return handleChatQuery(cq);
        else if (q instanceof UserQuery uq) return handleUserQuery(uq);
        else throw new UnknownQueryException(q);
    }

    private MessageResult handleMessageQuery(MessageQuery mq) {
        MessageResult mr = new MessageResult(mq);
        if (mq instanceof GetMessagesInChatQuery gmq) {
            List<Message> messages = connector.getMessagesFromChat(gmq.getChatID(), gmq.getOffset(), gmq.getLimit());
            mr.setSuccess(!messages.isEmpty());
            mr.add(messages);
        } else if (mq instanceof InsertMessageQuery imq) {
            connector.addMessage(
                    imq.getMessageID(),
                    imq.getChatID(),
                    imq.getAuthorID(),
                    imq.getContent()
            );
            mr.setSuccess(true);
        } else {
            throw new UnknownQueryException(mq);
        }
        return mr;
    }

    private ChatResult handleChatQuery(ChatQuery cq) {
        ChatResult cr = new ChatResult(cq);
        if (cq instanceof GetChatQuery gcq) {
            if (gcq instanceof GetUsersChatsQuery gucq) {
                List<Chat> usersChats = connector.getUsersChats(gucq.getChatID());
                cr.setSuccess(!usersChats.isEmpty());
                cr.add(usersChats);
            } else {
                Chat c = connector.getChat(gcq.getChatID());
                cr.setSuccess(c != null);
                cr.add(c);
            }
        } else if (cq instanceof InsertChatQuery icq) {
            connector.createChat(icq.getChatName(), icq.getMembers());
            cr.setSuccess(true);
        } else {
            throw new UnknownQueryException(cq);
        }
        return cr;
    }

    private UserResult handleUserQuery(UserQuery uq) {
        UserResult ur = new UserResult(uq);
        if (uq instanceof GetUserQuery guq) {
            if (guq instanceof LoginQuery lq) {
                LoginResult lr = new LoginResult(lq);
                EnumResults.LoginResult result = connector.login(lq.getUsername(), lq.getPassword());
                lr.setResult(result);
                switch (result) {
                    case LOGGED_IN -> lr.setSuccess(true);
                    case ACCOUNT_DOESNT_EXIST, WRONG_PASSWORD, SQL_EXCEPTION -> lr.setSuccess(false);
                }
                lr.add(connector.getUser(lq.getUsername()));
                return lr;
            } else if (guq instanceof GetMessageAuthorQuery gmaq) {
                ur.add(connector.getUser(gmaq.getUserID()));
                ur.setSuccess(true);
            } else {
                String username = guq.getUsername();
                ur.add(username == null ?
                        connector.getUser(guq.getUserID()) :
                        connector.getUser(username)
                );
                ur.setSuccess(true);
            }
        } else if (uq instanceof RegisterQuery rq) {
            RegisterResult rr = new RegisterResult(rq);
            EnumResults.RegisterResult result = connector.register(rq.getUsername(), rq.getHash());
            rr.setResult(result);
            switch (result) {
                case ACCOUNT_CREATED -> rr.setSuccess(true);
                case USERNAME_TAKEN, SQL_EXCEPTION -> rr.setSuccess(false);
            }
            rr.add(connector.getUser(rq.getUsername()));
            return rr;
        } else if (uq instanceof SendFriendRequestQuery sfrq) {
            switch (connector.sendFriendRequest(sfrq.getUserID(), sfrq.getFriendID())) {
                case SENT_SUCCESSFULLY -> ur.setSuccess(true);
                case SAME_USER, ALREADY_SENT, ALREADY_FRIENDS, SQL_EXCEPTION -> ur.setSuccess(false);
            }
        } else if (uq instanceof AcceptFriendRequestQuery afrq) {
            connector.acceptFriendRequest(afrq.getUserID(), afrq.getFriendID());
        } else if (uq instanceof DenyFriendRequestQuery dfrq) {
            connector.denyFriendRequest(dfrq.getUserID(), dfrq.getFriendID());
        } else if (uq instanceof RemoveFriendQuery rfq) {
            switch (connector.removeFriend(rfq.getUserID(), rfq.getFriendID())) {
                case REMOVED_FRIEND -> ur.setSuccess(true);
                case SAME_USER, SQL_EXCEPTION -> ur.setSuccess(false);
            }
        } else if (uq instanceof GetFriendsQuery gfq) {
            ur.add(connector.getFriends(gfq.getUserID()));
        } else if (uq instanceof GetFriendRequestsQuery gfrq) {
            ur.add(connector.getFriendRequests(gfrq.getUserID()));
        } else if (uq instanceof GetAllUsersQuery) {
            ur.add(connector.getAllUsers());
        } else {
            throw new UnknownQueryException(uq);
        }
        return ur;
    }
}
