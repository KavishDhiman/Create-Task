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

    @Value("${meenakshi.username}")
    private String meenakshiUsername;

    @Value("${meenakshi.password}")
    private String meenakshiPassword;

    @Value("${jayanthi.username}")
    private String jayanthiUsername;

    @Value("${jayanthi.password}")
    private String jayanthiPassword;

    @Value("${kaviya.username}")
    private String kaviyaUsername;

    @Value("${kaviya.password}")
    private String kaviyaPassword;

    @Value("${kavish.username}")
    private String kavishUsername;

    @Value("${kavish.password}")
    private String kavishPassword;

    @Value("${srihari.username}")
    private String srihariUsername;

    @Value("${srihari.password}")
    private String srihariPassword;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).authenticated()

                        // =========================================
                        // SRIHARI MODULE
                        // MUST COME BEFORE /api/v1/tasks/**
                        // =========================================
                        .requestMatchers(
                                "/attachment-comment-ui/**",
                                "/api/v1/attachments/**",
                                "/api/v1/comments/**",
                                "/api/v1/tasks/*/attachments",
                                "/api/v1/tasks/*/comments"
                        ).hasRole("ATTACHMENT")

                        // =========================================
                        // NOTIFICATION MODULE
                        // =========================================
                        .requestMatchers(
                                "/notifications-ui/**",
                                "/api/v1/notifications/**"
                        ).hasRole("NOTIFICATION")

                        // =========================================
                        // TASK MODULE
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

                        .requestMatchers(
                                "/api/v1/reports/tasks/overdue"
                        ).hasRole("TASK")

                        // =========================================
                        // PROJECT MODULE
                        // =========================================
                        .requestMatchers(
                                "/api/v1/users/*/projects"
                        ).hasRole("PROJECT")

                        .requestMatchers(
                                "/projects-ui/**",
                                "/api/v1/projects/**"
                        ).hasRole("PROJECT")

                        // =========================================
                        // NOTIFICATION USER ROUTE
                        // =========================================
                        .requestMatchers(
                                "/api/v1/users/*/notifications"
                        ).hasRole("NOTIFICATION")

                        // =========================================
                        // USER MANAGEMENT MODULE
                        // =========================================
                        .requestMatchers(
                                "/users-ui/**",
                                "/reports-ui/**",
                                "/api/v1/users/**",
                                "/api/v1/roles/**",
                                "/api/v1/reports/**"
                        ).hasRole("USER_MANAGEMENT")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", false)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        return new InMemoryUserDetailsManager(

                User.withUsername(meenakshiUsername)
                        .password(encoder.encode(meenakshiPassword))
                        .roles("NOTIFICATION")
                        .build(),

                User.withUsername(jayanthiUsername)
                        .password(encoder.encode(jayanthiPassword))
                        .roles("TASK")
                        .build(),

                User.withUsername(kaviyaUsername)
                        .password(encoder.encode(kaviyaPassword))
                        .roles("USER_MANAGEMENT")
                        .build(),

                User.withUsername(kavishUsername)
                        .password(encoder.encode(kavishPassword))
                        .roles("PROJECT")
                        .build(),

                User.withUsername(srihariUsername)
                        .password(encoder.encode(srihariPassword))
                        .roles("ATTACHMENT")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}