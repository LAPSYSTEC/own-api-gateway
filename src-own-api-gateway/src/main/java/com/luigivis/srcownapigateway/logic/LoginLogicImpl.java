package com.luigivis.srcownapigateway.logic;

import com.luigivis.srcownapigateway.dto.response.LoginResponseDto;
import com.luigivis.srcownapigateway.repository.UserRepository;
import com.luigivis.srcownapigateway.dto.response.StandardResponseDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Data
public class LoginLogicImpl {

    @Autowired
    private UserRepository userRepository;

    public StandardResponseDto<LoginResponseDto> login(String username, String password) {
        return null;
    }
}
