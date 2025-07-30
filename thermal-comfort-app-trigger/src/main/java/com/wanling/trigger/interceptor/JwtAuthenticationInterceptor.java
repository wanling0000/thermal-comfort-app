//package com.wanling.trigger.interceptor;
//
//import java.time.LocalDateTime;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.wanling.domain.user.service.JwtUtil;
//import com.wanling.types.security.LoginUserDTO;
//import com.wanling.types.exception.AppException;
//import com.wanling.types.security.LoginUserHolder;
//import io.jsonwebtoken.Claims;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationInterceptor implements HandlerInterceptor {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response,
//                             Object handler) {
//
//        log.info("🔐 JwtAuthenticationInterceptor called! path = {}", request.getRequestURI());
//
//        String token = request.getHeader("access-token");
//
//        log.info("🔐 Header Authorization = {}", token);
//
//        if (!StringUtils.hasText(token)) {
//            throw new AppException("AUTH_001", "Missing token");
//        }
//
//        Claims claims = jwtUtil.parseToken(token);
//
//        String userId = claims.get("userId", String.class);
//        String username = claims.get("username", String.class);
//        String email = claims.get("email", String.class);
//        LocalDateTime createdAt = LocalDateTime.parse(claims.get("createdAt", String.class));
//
//        LoginUserDTO loginUser = new LoginUserDTO(userId, username, email, createdAt);
//        LoginUserHolder.set(loginUser);
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request,
//                                HttpServletResponse response,
//                                Object handler,
//                                Exception ex) {
//        LoginUserHolder.clear();
//    }
//}
