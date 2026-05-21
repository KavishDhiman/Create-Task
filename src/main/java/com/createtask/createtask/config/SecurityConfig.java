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

                        // =========================================
                        // PUBLIC ROUTES
                        // =========================================

                        .requestMatchers(
                                "/",
                                "/login",
                                "/access-denied",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()

                        // =========================================
                        // SWAGGER
                        // =========================================

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).authenticated()

                        // =========================================
                        // ATTACHMENT + COMMENTS MODULE
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
                                "/api/v1/notifications/**",
                                "/api/v1/users/*/notifications"
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
                                "/api/v1/task-management/**",

                                "/api/v1/projects/*/tasks",
                                "/api/v1/users/*/tasks",

                                "/api/v1/reports/tasks/overdue"
                        ).hasRole("TASK")

                        // =========================================
                        // PROJECT MODULE
                        // =========================================

                        .requestMatchers(

                                "/projects-ui",
                                "/projects-ui/**",

                                "/reports-ui",
                                "/reports-ui/**",

                                "/api/v1/projects",
                                "/api/v1/projects/**",

                                "/api/v1/users/*/projects",

                                "/api/v1/reports/projects/summary"
                        ).hasRole("PROJECT")

                        // =========================================
                        // USER MANAGEMENT MODULE
                        // =========================================

                        .requestMatchers(
                                "/users-ui/**",
                                "/api/v1/users/**",
                                "/api/v1/roles/**",
                                "/api/v1/reports/users/**"
                        ).hasRole("USER_MANAGEMENT")

                        // =========================================
                        // EVERYTHING ELSE
                        // =========================================

                        .anyRequest().authenticated()
                )

                // =========================================
                // LOGIN
                // =========================================

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", false)
                        .permitAll()
                )

                // =========================================
                // LOGOUT
                // =========================================

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                // =========================================
                // ACCESS DENIED
                // =========================================

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    // =========================================
    // USERS
    // =========================================

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        return new InMemoryUserDetailsManager(

                // NOTIFICATION USER

                User.withUsername(meenakshiUsername)
                        .password(encoder.encode(meenakshiPassword))
                        .roles("NOTIFICATION")
                        .build(),

                // TASK USER

                User.withUsername(jayanthiUsername)
                        .password(encoder.encode(jayanthiPassword))
                        .roles("TASK")
                        .build(),

                // USER MANAGEMENT USER

                User.withUsername(kaviyaUsername)
                        .password(encoder.encode(kaviyaPassword))
                        .roles("USER_MANAGEMENT")
                        .build(),

                // PROJECT USER

                User.withUsername(kavishUsername)
                        .password(encoder.encode(kavishPassword))
                        .roles("PROJECT")
                        .build(),

                // ATTACHMENT USER

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