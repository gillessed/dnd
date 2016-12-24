package com.gillessed.dnd.rest.error;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableDndError.class)
@JsonDeserialize(as = ImmutableDndError.class)
public interface DndError {
    String LOGIN = "/login";
    String APP = "/app";
    String WIKI = "/app/wiki";
    String NONE = "";

    enum Type {
        INTERNAL_SERVER_ERROR("There was an internal server error. I have to fix something.", APP),
        LOGIN_FAILED("Invalid username or password. Please try again.", LOGIN),
        INVALID_SESSION("You are not logged in. Please log in again.", LOGIN),
        WIKI_PAGE_NOT_FOUND("The page you were looking for was not found."),
        NO_SUCH_USER("The user you are looking for does not exit."),
        ERROR_BUILDING_PAGE("The page file could not be parsed correctly. Please try again later.");

        private final String message;
        private final String redirect;
        Type(String message) {
            this(message, NONE);
        }

        Type(String message, String redirect) {
            this.message = message;
            this.redirect = redirect;
        }

        public DndError error() {
            return new Builder()
                    .errorType(this)
                    .errorMessage(message)
                    .redirect(redirect)
                    .build();
        }
    };

    Type getErrorType();
    String getErrorMessage();
    String getRedirect();

    class Builder extends ImmutableDndError.Builder {}
}
