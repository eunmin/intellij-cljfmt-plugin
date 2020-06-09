package com.intellij.plugin.cljfmt.exception;

public class ReformatException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Reformat failed";
    }
}
