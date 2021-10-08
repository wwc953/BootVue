package com.sg.vue.exception;

public class CheckTokenException extends RuntimeException{
    public CheckTokenException() {
        super();
    }

    public CheckTokenException(String message) {
        super(message);
    }

    public CheckTokenException(Throwable cause) {
        super(cause);
    }
}
