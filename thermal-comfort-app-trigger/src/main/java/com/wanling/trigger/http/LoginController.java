package com.wanling.trigger.http;

import com.wanling.domain.user.service.ILoginService;
import com.wanling.trigger.api.dto.login.LoginRequestDTO;
import com.wanling.trigger.api.dto.login.LoginResponseDTO;
import com.wanling.trigger.api.dto.login.RegisterRequestDTO;
import com.wanling.types.security.LoginUserDTO;
import com.wanling.trigger.assembler.LoginAssembler;
import com.wanling.types.enums.ResponseCode;
import com.wanling.types.model.Response;
import com.wanling.types.security.LoginUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

    private final ILoginService loginService;

    @PostMapping("/login")
    public Response<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        String token = loginService.login(LoginAssembler.toVO(dto));
        return Response.<LoginResponseDTO>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("Login success")
                       .data(LoginAssembler.toDTO(token))
                       .build();
    }

    @GetMapping("/info")
    public Response<LoginUserDTO> getInfo() {
        return Response.<LoginUserDTO>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("User info loaded")
                       .data(LoginAssembler.toDTO(loginService.getLoginInfo()))
                       .build();
    }

    @PostMapping("/register")
    public Response<Void> register(@RequestBody RegisterRequestDTO request) {
        loginService.register(LoginAssembler.toVO(request));
        return Response.<Void>builder()
                       .code(ResponseCode.SUCCESS.getCode())
                       .info("Register success")
                       .build();
    }

}
