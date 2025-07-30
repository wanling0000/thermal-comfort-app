package com.wanling.domain.user.model.entity;

import java.time.LocalDateTime;

public record UserEntity (
        String userId,
        String name,
        String email,
        String password,
        LocalDateTime createdAt
)
 { }
