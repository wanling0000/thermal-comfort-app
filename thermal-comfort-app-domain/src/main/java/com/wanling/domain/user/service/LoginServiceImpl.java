package com.wanling.domain.user.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.wanling.domain.user.model.entity.UserEntity;
import com.wanling.domain.user.model.valobj.LoginRequestVO;
import com.wanling.domain.user.model.valobj.LoginUserVO;
import com.wanling.domain.user.model.valobj.RegisterUserVO;
import com.wanling.domain.user.repository.IUserRepository;
import com.wanling.domain.user.service.ILoginService;
import com.wanling.types.enums.LoginErrorCode;
import com.wanling.types.exception.AppException;
import com.wanling.types.security.LoginUserDTO;
import com.wanling.types.security.LoginUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements ILoginService {

    private final IUserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public String login(LoginRequestVO loginVO) {
        // 1. 查询用户
        UserEntity user = loginVO.username()
                                 .map(userRepository::findByUsername)
                                 .orElseGet(() -> loginVO.email()
                                                         .map(userRepository::findByEmail)
                                                         .orElseThrow(LoginErrorCode.MISSING_CREDENTIALS::asException))
                                 .orElseThrow(LoginErrorCode.ACCOUNT_NOT_FOUND::asException);

        log.info("🧾 UserEntity createdAt = {}", user.createdAt());

        // 2. 校验密码
        if (!user.password().equals(DigestUtils.md5Hex(loginVO.password()))) {
            throw LoginErrorCode.INVALID_CREDENTIALS.asException();
        }

        // 3. 生成并返回 JWT
        return jwtUtil.createToken(
                user.userId(),
                user.name(),
                user.email(),
                user.createdAt()
        );
    }

    @Override
    public void register(RegisterUserVO vo) {
        // 1. 检查邮箱是否已注册
        if (userRepository.findByEmail(vo.email()).isPresent()) {
            throw new AppException("REGISTER_001", "Email already registered");
        }

        // 2. 检查用户名是否已注册
        if (userRepository.findByUsername(vo.username()).isPresent()) {
            throw new AppException("REGISTER_002", "Username already registered");
        }

        // 3. 构建 UserEntity（注意加密密码）
        UserEntity user = new UserEntity(
                null,
                vo.username(),
                vo.email(),
                DigestUtils.md5Hex(vo.password()),
                LocalDateTime.now()
        );

        // 4. 保存用户
        userRepository.save(user);
    }

    @Override
    public LoginUserVO getLoginInfo() {
        LoginUserDTO loginUser = LoginUserHolder.get();
        return userRepository.findById(loginUser.userId())
                             .map(user -> new LoginUserVO(user.userId(), user.name(), user.email(), user.createdAt()))
                             .orElseThrow(() -> new AppException("AUTH_003", "User not found"));
    }
}
