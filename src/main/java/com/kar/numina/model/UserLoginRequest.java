package com.kar.numina.model;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String email;
    private String password;
}

