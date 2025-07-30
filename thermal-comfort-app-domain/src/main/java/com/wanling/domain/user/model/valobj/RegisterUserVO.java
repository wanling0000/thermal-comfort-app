package com.wanling.domain.user.model.valobj;

public record RegisterUserVO(
    String username,
    String email,
    String password
) {}