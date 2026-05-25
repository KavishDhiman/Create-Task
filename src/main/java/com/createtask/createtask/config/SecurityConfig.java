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

@Configuration // Marks this class as a Spring configuration class
@EnableWebSecurity // Enables Spring Security for the application
public class SecurityConfig {

    @Value("${meenakshi.username}") // Injects notification module username from properties file
    private String meenakshiUsername;

    @Value("${meenakshi.password}") // Injects notification module password from properties file
    private String meenakshiPassword;

    @Value("${jayanthi.username}") // Injects task module username from properties file
    private String jayanthiUsername;

    @Value("${jayanthi.password}") // Injects task module password from properties file
    private String jayanthiPassword;

    @Value("${kaviya.username}") // Injects user management module username from properties file
    private String kaviyaUsername;

    @Value("${kaviya.password}") // Injects user management module password from properties file
    private String kaviyaPassword;

    @Value("${kavish.username}") // Injects project module username from properties file
    private String kavishUsername;

    @Value("${kavish.password}") // Injects project module password from properties file
    private String kavishPassword;

    @Value("${srihari.username}") // Injects attachment module username from properties file
    private String srihariUsername;

    @Value("${srihari.password}") // Injects attachment module password from properties file
    private String srihariPassword;

    @Bean // Registers SecurityFilterChain as a Spring bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Configures application security rules, authentication, and authorization

        http

                .csrf(csrf -> csrf.disable()) // Disables CSRF protection for the application

                .authorizeHttpRequests(auth -> auth // Configures authorization rules for incoming requests

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
                        ).permitAll() // Allows unrestricted access to public resources

                        // =========================================
                        // SWAGGER
                        // =========================================

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).authenticated() // Allows only authenticated users to access Swagger documentation

                        // =========================================
                        // ATTACHMENT + COMMENTS MODULE
                        // =========================================

                        .requestMatchers(
                                "/attachment-comment-ui/**",
                                "/api/v1/attachments/**",
                                "/api/v1/comments/**",
                                "/api/v1/tasks/*/attachments",
                                "/api/v1/tasks/*/comments"
                        ).hasRole("ATTACHMENT") // Restricts attachment and comment APIs to ATTACHMENT role

                        // =========================================
                        // NOTIFICATION MODULE
                        // =========================================

                        .requestMatchers(
                                "/notifications-ui/**",
                                "/api/v1/notifications/**",
                                "/api/v1/users/*/notifications"
                        ).hasRole("NOTIFICATION") // Restricts notification APIs to NOTIFICATION role

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
                        ).hasRole("TASK") // Restricts task-related APIs to TASK role

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
                        ).hasRole("PROJECT") // Restricts project-related APIs to PROJECT role

                        // =========================================
                        // USER MANAGEMENT MODULE
                        // =========================================

                        .requestMatchers(
                                "/users-ui/**",
                                "/api/v1/users/**",
                                "/api/v1/roles/**",
                                "/api/v1/reports/users/**"
                        ).hasRole("USER_MANAGEMENT") // Restricts user management APIs to USER_MANAGEMENT role

                        // =========================================
                        // EVERYTHING ELSE
                        // =========================================

                        .anyRequest().authenticated() // Requires authentication for all remaining requests
                )

                // =========================================
                // LOGIN
                // =========================================

                .formLogin(form -> form // Configures custom login functionality
                        .loginPage("/login")
                        .defaultSuccessUrl("/", false)
                        .permitAll()
                )

                // =========================================
                // LOGOUT
                // =========================================

                .logout(logout -> logout // Configures logout functionality
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                // =========================================
                // ACCESS DENIED
                // =========================================

                .exceptionHandling(ex -> ex // Configures custom access denied handling
                        .accessDeniedPage("/access-denied")
                );

        return http.build(); // Builds and returns the configured security filter chain
    }

    // =========================================
    // USERS
    // =========================================

    @Bean // Registers UserDetailsService as a Spring bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) { // Creates in-memory users with role-based access configuration

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
        ); // Creates and stores all configured in-memory users
    }

    @Bean // Registers PasswordEncoder as a Spring bean
    public PasswordEncoder passwordEncoder() { // Creates password encoder bean for secure password encryption
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // Returns delegating password encoder supporting multiple encodings
    }
}