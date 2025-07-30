package com.wanling.types.security;

public class LoginUserHolder {
    private static final ThreadLocal<LoginUserDTO> HOLDER = new ThreadLocal<>();

    public static void set(LoginUserDTO user) {
        HOLDER.set(user);
    }

    public static LoginUserDTO get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
