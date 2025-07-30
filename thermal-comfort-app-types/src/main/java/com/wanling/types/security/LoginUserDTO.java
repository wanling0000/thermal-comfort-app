package com.wanling.types.security;

import java.time.LocalDateTime;

public record LoginUserDTO (
        String userId,
        String username,
        String email,
        LocalDateTime createdAt
) { }
