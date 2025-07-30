package com.wanling.trigger.api.dto.login;

public record RegisterRequestDTO(
    String username,
    String email,
    String password
) {}