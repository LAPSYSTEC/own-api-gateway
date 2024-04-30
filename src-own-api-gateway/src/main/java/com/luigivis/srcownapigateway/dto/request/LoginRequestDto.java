package com.luigivis.srcownapigateway.dto.request;


import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(@NotBlank(message = "Username Can't be null")
                              String username,
                              @NotBlank(message = "Password Can't be null")
                              String password) {

}
