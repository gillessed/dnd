package com.gillessed.dnd.model.auth;

public class Session {
    private final String token;
    private final User user;

    public Session(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (token != null ? !token.equals(session.token) : session.token != null) return false;
        return user != null ? user.equals(session.user) : session.user == null;

    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Session{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
