package com.levimartines.codewithspring.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class PrincipalService {

    public static CustomUserDetails authenticated() {
        try {
            return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}
