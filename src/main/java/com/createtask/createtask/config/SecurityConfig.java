package com.createtask.createtask.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Meenakshi Credentials
    // =========================================
    @Value("${meenakshi.username}")
    private String meenakshiUsername;

    @Value("${meenakshi.password}")
    private String meenakshiPassword;

    // =========================================
    // Jayanthi Credentials
    // =========================================
    @Value("${jayanthi.username}")
    private String jayanthiUsername;

    @Value("${jayanthi.password}")
    private String jayanthiPassword;

    // =========================================
    // Kaviya Credentials
    // =========================================
    @Value("${kaviya.username}")
    private String kaviyaUsername;

    @Value("${kaviya.password}")
    private String kaviyaPassword;

    // =========================================
    // Kavish Credentials
    // =========================================
    @Value("${kavish.username}")
    private String kavishUsername;

    @Value("${kavish.password}")
    private String kavishPassword;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // =========================================
                        // Public Routes
                        // =========================================
                        .requestMatchers(
                                "/",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // =========================================
                        // Swagger Routes
                        // =========================================
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).authenticated()

                        // =========================================
                        // Notification Module
                        // =========================================
                        .requestMatchers(
                                "/notifications-ui/**",
                                "/api/v1/notifications/**"
                        ).hasRole("NOTIFICATION")

                        // =========================================
                        // Jayanthi Module
                        // Task / Category / TaskCategory / Read Chats / API Overview
                        // =========================================
                        .requestMatchers(
                                "/tasks-ui/**",
                                "/categories-ui/**",
                                "/taskcategories-ui/**",
                                "/read-chats-ui/**",
                                "/task-management-api-overview/**",
                                "/api/v1/tasks/**",
                                "/api/v1/categories/**",
                                "/api/v1/taskcategories/**",
                                "/api/v1/chats/**",
                                "/api/v1/task-management/**"
                        ).hasRole("TASK")

                        .requestMatchers(
                                "/api/v1/projects/*/tasks",
                                "/api/v1/users/*/tasks"
                        ).hasRole("TASK")

                        // =========================================
                        // PROJECT USER ROUTE
                        // MUST COME BEFORE /api/v1/users/**
                        // =========================================
                        .requestMatchers(
                                "/api/v1/users/*/projects"
                        ).hasRole("PROJECT")

                        // PROJECT USER ROUTE MUST COME BEFORE /api/v1/users/**
                        .requestMatchers(
                                "/api/v1/users/*/projects"
                        ).hasRole("PROJECT")

                        // Jayanthi / TASK report access
                        .requestMatchers(
                                "/api/v1/reports/tasks/overdue"
                        ).hasRole("TASK")


                        // NOTIFICATION USER ROUTE MUST COME BEFORE /api/v1/users/**
                        .requestMatchers(
                                "/api/v1/users/*/notifications"
                        ).hasRole("NOTIFICATION")

                        // Users & Roles Module
                        .requestMatchers(
                                "/users-ui/**",
                                "/comments-ui/**",
                                "/reports-ui/**",
                                "/api/v1/users/**",
                                "/api/v1/roles/**",
                                "/api/v1/reports/**"
                        ).hasRole("USER_MANAGEMENT")

                        // =========================================
                        // Projects Module
                        // =========================================
                        .requestMatchers(
                                "/projects-ui/**",
                                "/api/v1/projects/**"
                        ).hasRole("PROJECT")

                        // =========================================
                        // Everything Else
                        // =========================================
                        .anyRequest().authenticated()
                )

                // =========================================
                // Login Configuration
                // =========================================
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", false)
                        .permitAll()
                )

                // =========================================
                // Logout Configuration
                // =========================================
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                // =========================================
                // Access Denied Page
                // =========================================
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    // =========================================
    // In-Memory Users
    // =========================================
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        return new InMemoryUserDetailsManager(

                // Meenakshi
                User.withUsername(meenakshiUsername)
                        .password(encoder.encode(meenakshiPassword))
                        .roles("NOTIFICATION")
                        .build(),

                // Jayanthi
                User.withUsername(jayanthiUsername)
                        .password(encoder.encode(jayanthiPassword))
                        .roles("TASK")
                        .build(),

                // Kaviya
                User.withUsername(kaviyaUsername)
                        .password(encoder.encode(kaviyaPassword))
                        .roles("USER_MANAGEMENT")
                        .build(),

                // Kavish
                User.withUsername(kavishUsername)
                        .password(encoder.encode(kavishPassword))
                        .roles("PROJECT")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}