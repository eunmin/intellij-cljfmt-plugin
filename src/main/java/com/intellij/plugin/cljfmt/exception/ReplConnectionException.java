package com.intellij.plugin.cljfmt.exception;

public class ReplConnectionException extends RuntimeException {
    private String host;

    private int port;

    public ReplConnectionException(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String getMessage() {
        return String.format("Could not connect to nREPL (%s:%d)", host, port);
    }
}
