package com.github.jacekpoz.common;

public class User {

    private long id;
    private String nickname;
    private String hashedPassword;

    public User() {}

    public User(String nick, String hash, long id) {
        initUser(nick, hash, id);
    }

    public void initUser(String nick, String hash, long id) {
        nickname = nick;
        hashedPassword = hash;
        this.id = id;
    }

    public void initUser(User u) {
        initUser(u.getNickname(), u.getHashedPassword(), u.getId());
    }

    public long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
