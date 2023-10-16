package com.dmdev.spring.dto;

import liquibase.pro.packaged.V;
import lombok.Value;

@Value
public class LoginDto {
    String username;
    String password;
}
