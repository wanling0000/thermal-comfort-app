package com.wanling.domain.user.model.valobj;

public record CaptchaVO(
        String key,
        String base64Image
) {}
