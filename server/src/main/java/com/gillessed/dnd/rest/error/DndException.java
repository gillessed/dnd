package com.gillessed.dnd.rest.error;

public class DndException extends RuntimeException {
    private final DndError error;

    public DndException(DndError error, Throwable e) {
        super(e);
        this.error = error;
    }

    public DndException(DndError error) {
        super();
        this.error = error;
    }

    public DndError getError() {
        return error;
    }
}
