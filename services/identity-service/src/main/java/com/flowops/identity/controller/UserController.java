package com.flowops.identity.controller;

import com.flowops.identity.entity.User;
import com.flowops.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Admin endpoint";
    }

    @GetMapping("/it")
    @PreAuthorize("hasAnyRole('IT_AGENT', 'ADMIN')")
    public String itOnly() {
        return "IT endpoint";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public String managerOnly() {
        return "Manager endpoint";
    }
}