package com.github.jacekpoz.common;

public class FriendException extends Exception {
    private static final long serialVersionUID = -8418528735243676557L;

    public FriendException() {
        super();
    }

    public FriendException(String message) {
        super(message);
    }

    public FriendException(String message, Throwable cause) {
        super(message, cause);
    }

    public FriendException(Throwable cause) {
        super(cause);
    }
}
