package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.UserLoginDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthControllerIntegrationTestUtil {

    protected static final String REGISTER_URL = "/auth/register";
    protected static final String LOGIN_URL = "/auth/login";
    protected static final String VALID_USERNAME = "test";
    protected static final String VALID_EMAIL = "test@test.com";
    protected static final String VALID_PASSWORD = "Test!1234";
    protected static final String EXISTENT_EMAIL = "admin@gmail.com";
    protected static final String INVALID_PASSWORD = "test123456";
    protected static final String INVALID_EMAIL = "email.com";
    protected static final String INVALID_USERNAME = "user name";

    protected UserRegistrationDto createUserRegistrationDto(String username, String email, String password,
                                                            String confirmPassword) {
        return new UserRegistrationDto(username, email, password, confirmPassword);
    }

    protected UserLoginDto createUserAuthenticationDto(String username, String password) {
        return new UserLoginDto(username, password);
    }
}