package com.gillessed.dnd.bundles.auth;

/**
 * Created by gcole on 8/16/16.
 */
public class DndBearerTokenCredentialImpl implements DndBearerTokenCredential {
    private final String bearerToken;

    public DndBearerTokenCredentialImpl(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    @Override
    public String getBearerToken() {
        return bearerToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DndBearerTokenCredentialImpl that = (DndBearerTokenCredentialImpl) o;

        return bearerToken != null ? bearerToken.equals(that.bearerToken) : that.bearerToken == null;

    }

    @Override
    public int hashCode() {
        return bearerToken != null ? bearerToken.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DndBearerTokenCredentialImpl{" +
                super.toString() +
                '}';
    }
}
