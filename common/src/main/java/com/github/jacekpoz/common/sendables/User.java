package com.github.jacekpoz.common.sendables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Sendable {

    @ToString.Include
    @EqualsAndHashCode.Include
    @Getter
    private final long id;
    @ToString.Include
    @Getter
    @Setter
    private String nickname;
    @Getter
    @Setter
    private String hashedPassword;
    @Getter
    private final List<Long> friendsIds;
    @Getter
    private final LocalDateTime dateJoined;

    @JsonCreator
    public User(
            @JsonProperty("userID") long userID,
            @JsonProperty("username") String userNickname,
            @JsonProperty("hashedPassword") String userHashedPassword,
            @JsonProperty("dateJoined") LocalDateTime date
    ) {
        id = userID;
        nickname = userNickname;
        hashedPassword = userHashedPassword;
        friendsIds = new ArrayList<>();
        dateJoined = date;
    }

    public void addFriend(User u) {
        if (!friendsIds.contains(u.id)) friendsIds.add(u.id);
    }

    public void addFriend(long id) {
        if (!friendsIds.contains(id)) friendsIds.add(id);
    }

    public void removeFriend(User u) {
        friendsIds.remove(u.id);
    }

    public void removeFriend(long id) {
        friendsIds.remove(id);
    }

}
