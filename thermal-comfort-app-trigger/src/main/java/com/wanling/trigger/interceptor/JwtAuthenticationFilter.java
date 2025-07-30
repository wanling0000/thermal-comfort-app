package com.wanling.trigger.interceptor;

import com.wanling.domain.user.service.JwtUtil;
import com.wanling.types.security.LoginUserDTO;
import com.wanling.types.security.LoginUserHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // 🛑 公共路径放行
        if (path.startsWith("/api/v1/auth/login") || path.startsWith("/api/v1/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        log.info("🔐 Header Authorization = {}", header);

        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            handleUnauthorized(response, "AUTH_001", "Missing token");
            return;
        }

        String token = header.substring(7).trim();
        log.info("🔐 JwtAuthenticationFilter path = {}", path);
        log.info("🔐 Header access-token = {}", token);

        try {
            Claims claims = jwtUtil.parseToken(token);
            String userId = claims.get("userId", String.class);
            String username = claims.get("username", String.class);
            String email = claims.get("email", String.class);
            LocalDateTime createdAt = LocalDateTime.parse(claims.get("createdAt", String.class));

            LoginUserDTO loginUser = new LoginUserDTO(userId, username, email, createdAt);
            LoginUserHolder.set(loginUser);

            try {
                filterChain.doFilter(request, response);
            } finally {
                LoginUserHolder.clear();
            }

        } catch (ExpiredJwtException e) {
            log.warn("🔐 JWT expired: {}", e.getMessage());
            handleUnauthorized(response, "AUTH_002", "Token expired");
        } catch (Exception e) {
            log.error("🔐 JWT validation failed: {}", e.getMessage());
            handleUnauthorized(response, "AUTH_001", "Invalid token");
        }
    }

    private void handleUnauthorized(HttpServletResponse response, String code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String json = String.format("{\"code\": \"%s\", \"info\": \"%s\"}", code, message);
        response.getWriter().write(json);
    }
}
