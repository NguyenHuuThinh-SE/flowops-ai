package com.flowops.identity.config;

import com.flowops.identity.entity.Role;
import com.flowops.identity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        List<String> roles = List.of(
                "ROLE_EMPLOYEE",
                "ROLE_IT_AGENT",
                "ROLE_MANAGER",
                "ROLE_ADMIN"
        );

        for (String roleName : roles) {

            if (roleRepository.findByName(roleName).isEmpty()) {

                roleRepository.save(
                        Role.builder()
                                .name(roleName)
                                .build()
                );
            }
        }
    }
}
