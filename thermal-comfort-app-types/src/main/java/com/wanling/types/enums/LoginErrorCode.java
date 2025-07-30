package com.wanling.types.enums;

import com.wanling.types.exception.AppException;

public enum LoginErrorCode {
    MISSING_CREDENTIALS("LOGIN_001", "Username or email must be provided"),
    ACCOUNT_NOT_FOUND("LOGIN_002", "Account does not exist"),
    INVALID_CREDENTIALS("LOGIN_003", "Invalid username/email or password");

    private final String code;
    private final String message;

    LoginErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public AppException asException() {
        return new AppException(code, message);
    }

    public String code() { return code; }
    public String message() { return message; }
}
