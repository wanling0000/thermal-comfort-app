package com.wanling.domain.user.model.valobj;

import java.time.LocalDateTime;

public record LoginUserVO(
        String userId,
        String username,
        String email,
        LocalDateTime createdAt) {}