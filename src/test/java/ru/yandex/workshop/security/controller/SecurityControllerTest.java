package ru.yandex.workshop.security.controller;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.workshop.security.config.JwtTokenProvider;
import ru.yandex.workshop.security.config.WebSecurityConfigurer;
import ru.yandex.workshop.security.dto.JwtRequest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = WebSecurityConfigurer.class)
public class SecurityControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    static JwtRequest jwtRequest;
    static String token;

    @BeforeAll
    static void init(){
        jwtRequest = JwtRequest.builder()
                .email("aaa@aaa.ru")
                .password("aaa")
                .build();
        token = "secret";
    }


    @ExtendWith(SpringExtension.class)
    @WithMockUser(value = "spring")
    @Test
    public void auth() throws Exception {
        when(jwtTokenProvider.generateToken(anyString(), anyString()))
                .thenReturn(token);

        mvc.perform(post("/auth/login")
                        .content(mapper.writeValueAsString(jwtRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
