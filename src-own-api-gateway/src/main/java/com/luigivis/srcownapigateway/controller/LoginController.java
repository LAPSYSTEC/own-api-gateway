package com.luigivis.srcownapigateway.controller;

import com.luigivis.srcownapigateway.dto.request.LoginRequestDto;
import com.luigivis.srcownapigateway.dto.response.LoginResponseDto;
import com.luigivis.srcownapigateway.logic.LoginLogicImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.luigivis.srcownapigateway.dto.response.StandardResponseDto.GenerateHttpResponse;

@RestController
@RequestMapping("/auth/login")
public class LoginController {

    @PostMapping("/login")
    public Object<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        var response = LoginLogicImpl.login(loginRequestDto);
        return GenerateHttpResponse(response);

    }
}
