package com.flowops.ticket.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUser {
    public UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof FlowOpsUserPrincipal user)) {
            throw new IllegalStateException("nvalid authenticated principal");
        }

        return user.userId();
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public boolean hasRole(String role) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority
                                .getAuthority()
                                .equals(role)
                );
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public boolean isManager() {
        return hasRole("ROLE_MANAGER");
    }

    public boolean isItAgent() {
        return hasRole("ROLE_IT_AGENT");
    }
}
