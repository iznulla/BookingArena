package com.booking.arena.configuration;

import com.booking.arena.entity.user.*;
import com.booking.arena.repository.PrivilegeRepository;
import com.booking.arena.repository.RoleRepository;
import com.booking.arena.repository.UserRepository;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class ProjectConfig {
    private final UserRepository userRepository;
    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()));
    }

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    private void initDefaultUsers() {
        if (userRepository.findByUsername("admin").isPresent())
            return;
        Privilege privilege = Privilege.builder()
                .name("ALL")
                .build();
        privilegeRepository.save(privilege);
        RoleEntity role = RoleEntity.builder()
                .name("ADMIN")
                .build();

        RolePrivilege rolePrivilege = new RolePrivilege();
        rolePrivilege.setPrivilege(privilege);
        rolePrivilege.setRole(role);
//        roleRepository.save(role);

//        rolePrivilegeRepository.save(rolePrivilege);

        roleRepository.save(role);

        UserEntity admin = UserEntity.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("admin@mail.ru")
                .role(role)
                .isActive(true)
                .build();
        UserProfile userProfile = UserProfile
                .builder()
                .user(admin)
                .address(null)
                .name("admin")
                .surname("admin")
                .build();
        admin.setUserProfile(userProfile);
        userRepository.save(admin);
    }
}
