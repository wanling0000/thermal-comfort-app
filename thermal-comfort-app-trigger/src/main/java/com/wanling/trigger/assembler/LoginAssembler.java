package com.wanling.trigger.assembler;

import com.wanling.domain.user.model.valobj.LoginRequestVO;
import com.wanling.domain.user.model.valobj.LoginUserVO;
import com.wanling.domain.user.model.valobj.RegisterUserVO;
import com.wanling.trigger.api.dto.login.LoginRequestDTO;
import com.wanling.trigger.api.dto.login.LoginResponseDTO;
import com.wanling.trigger.api.dto.login.RegisterRequestDTO;
import com.wanling.types.security.LoginUserDTO;

public class LoginAssembler {

    public static LoginRequestVO toVO(LoginRequestDTO dto) {
        return new LoginRequestVO(
                dto.username(),
                dto.email(),
                dto.password()
        );
    }

    public static LoginResponseDTO toDTO(String token) {
        return new LoginResponseDTO(token);
    }

    public static RegisterUserVO toVO(RegisterRequestDTO dto) {
        return new RegisterUserVO(dto.username(), dto.email(), dto.password());
    }

    public static LoginUserDTO toDTO(LoginUserVO vo) {
        return new LoginUserDTO(vo.userId(), vo.username(), vo.email(), vo.createdAt());
    }
}
