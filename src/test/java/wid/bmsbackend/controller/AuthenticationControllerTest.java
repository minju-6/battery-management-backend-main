package wid.bmsbackend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wid.bmsbackend.service.JwtService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {
    @MockBean
    AuthenticationController authenticationController;
    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    JwtService jwtService;
    @MockBean
    UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("인증이 실패하면 4xx을 반환한다.")
    void authenticateFail() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenThrow(new BadCredentialsException("error"));
        // given
        String content = "{\"username\":\"test\", \"password\":\"test\"}";
        // when
        ResultActions resultAction = mvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        var mvcResult = resultAction.andExpect(status().is4xxClientError()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

}