package com.wanling.domain.user.service;

import com.wanling.domain.user.model.valobj.LoginRequestVO;
import com.wanling.domain.user.model.valobj.LoginUserVO;
import com.wanling.domain.user.model.valobj.RegisterUserVO;

public interface ILoginService {
    String login(LoginRequestVO loginRequestVO);

    void register(RegisterUserVO vo);

    LoginUserVO getLoginInfo();
}
