package com.wanling.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "Success"),
    UNKNOWN_ERROR("0001", "Unknown error"),
    INVALID_PARAMETER("0002", "Invalid parameter"),
    ;

    private String code;
    private String info;

}
