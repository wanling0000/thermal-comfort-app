package com.wanling.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum ResponseCode {
    SUCCESS("0000", "Success"),
    UNKNOWN_ERROR("0001", "Unknown error"),
    INVALID_PARAMETER("0002", "Invalid parameter");

    private final String code;
    private final String info;

    ResponseCode(String code, String info) {
        this.code = code;
        this.info = info;
    }
}
