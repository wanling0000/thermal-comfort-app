package com.wanling.test.domain.login;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanling.trigger.api.dto.login.LoginRequestDTO;
import com.wanling.trigger.api.dto.login.RegisterRequestDTO;
import com.wanling.types.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequestDTO registerDTO;
    private LoginRequestDTO loginDTO;

    @BeforeEach
    void setup() {
//        registerDTO = new RegisterRequestDTO("testuser", "test@example.com", "123456");
//        loginDTO = new LoginRequestDTO(Optional.of("testuser"), Optional.empty(), "123456");
        registerDTO = new RegisterRequestDTO("admin", "admin@example.com", "admin");
        loginDTO = new LoginRequestDTO(Optional.of("admin"), Optional.empty(), "admin");
    }
    @Test
    @DisplayName("🟢 用户注册成功")
    void testRegisterSuccess() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
               .andExpect(status().isOk());
    }
    @Test
    @DisplayName("🔴 用户重复注册失败")
    void testRegisterDuplicateEmail() throws Exception {
        // 第一次注册成功
        mockMvc.perform(post("/api/v1/auth/register")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(registerDTO)))
               .andExpect(status().isOk());

        // 第二次注册
        var result = mockMvc.perform(post("/api/v1/auth/register")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(registerDTO)))
                            .andExpect(status().isOk()) // 因为你的系统即使失败也返回200
                            .andReturn();

        // 解析 Response
        var response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);

        // 判断 code 是业务失败码
        assertThat(response.getCode()).isEqualTo("REGISTER_001");
    }

    @Test
    @DisplayName("🟢 用户登录成功")
    void testLoginSuccess() throws Exception {
        // 确保先注册
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
               .andExpect(status().isOk());

        var result = mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO)))
                            .andExpect(status().isOk())
                            .andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
        assertThat(response.getCode()).isEqualTo("0000");
        assertThat(response.getData()).isNotNull();
    }

    @Test
    @DisplayName("🔴 用户登录失败（密码错误）")
    void testLoginWrongPassword() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
               .andExpect(status().isOk());

        var wrongLogin = new LoginRequestDTO(Optional.of("admin"), Optional.empty(), "wrongpwd");

        var result = mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(wrongLogin)))
                            .andExpect(status().isOk())
                            .andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
        assertThat(response.getCode()).isNotEqualTo("00000");
    }

    @Test
    @DisplayName("🟢 获取当前登录用户信息")
    void testGetLoginInfo() throws Exception {
        // 1. 注册
        mockMvc.perform(post("/api/v1/auth/register")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(registerDTO)))
               .andExpect(status().isOk());

        // 2. 登录获取 token
        var loginResult = mockMvc.perform(post("/api/v1/auth/login")
                                         .contentType(MediaType.APPLICATION_JSON)
                                         .content(objectMapper.writeValueAsString(loginDTO)))
                                 .andExpect(status().isOk())
                                 .andReturn();

        var loginResponse = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        var token = loginResponse.path("data").path("token").asText();
        assertThat(token).isNotEmpty();

        // 3. 携带 token 调用 /info
        var infoResult = mockMvc.perform(get("/api/v1/auth/info")
                                        .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andReturn();


        var infoJson = objectMapper.readTree(infoResult.getResponse().getContentAsString());
        var code = infoJson.path("code").asText();
        var user = infoJson.path("data");

        assertThat(code).isEqualTo("0000");
        assertThat(user.path("username").asText()).isEqualTo("admin");
        assertThat(user.path("email").asText()).isEqualTo("admin@example.com");
        assertThat(user.path("created_at").asText()).isNotEmpty();

        System.out.println("✅ User Info: " + user.toPrettyString());
    }

}