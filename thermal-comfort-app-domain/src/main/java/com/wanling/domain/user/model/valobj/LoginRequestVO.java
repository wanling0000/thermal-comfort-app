package com.wanling.domain.user.model.valobj;

import java.util.Optional;

public record LoginRequestVO(
    Optional<String> username,
    Optional<String> email,
    String password
) {}
