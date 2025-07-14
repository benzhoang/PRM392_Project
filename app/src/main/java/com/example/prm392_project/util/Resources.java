package com.example.prm392_project.util;

public abstract class Resources<T> {
    private final T data;
    private final String message;

    public Resources(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public T getData() { return data; }
    public String getMessage() { return message; }

    public static final class Success<T> extends Resources<T> {
        public Success(T data) {
            super(data, null);
        }
    }

    public static final class Error<T> extends Resources<T> {
        public Error(String message, T data) {
            super(data, message);
        }
        public Error(String message) {
            super(null, message);
        }
    }

    public static final class Loading<T> extends Resources<T> {
        private final boolean isLoading;
        public Loading() {
            super(null, null);
            this.isLoading = true;
        }
        public Loading(boolean isLoading) {
            super(null, null);
            this.isLoading = isLoading;
        }
        public boolean isLoading() { return isLoading; }
    }
}
