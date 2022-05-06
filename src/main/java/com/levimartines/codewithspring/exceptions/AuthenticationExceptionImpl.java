package com.levimartines.codewithspring.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationExceptionImpl extends AuthenticationException {
    public AuthenticationExceptionImpl(String msg, Throwable cause) {
        super(msg, cause);
    }

}
