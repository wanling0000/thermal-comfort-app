package com.wanling.trigger.api.dto.login;

import java.util.Optional;

public record LoginRequestDTO (
        Optional<String> username,
        Optional<String> email,
        String password
) { }
