package com.wanling.types.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author fwl
 * @Description
 * @Date 29/09/2024 17:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {
    private String code;
    private String info;
    private T data;

    // 成功响应（无数据）
    public static <T> Response<T> ok() {
        return new Response<>("0000", "Success", null);
    }

    // 成功响应（含数据）
    public static <T> Response<T> ok(T data) {
        return new Response<>("0000", "Success", data);
    }

    // 失败响应
    public static <T> Response<T> fail(String code, String message) {
        return new Response<>(code, message, null);
    }
}
