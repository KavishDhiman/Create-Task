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

    // ── Loaded from Vault ──
    @Value("${meenakshi.username}")
    private String meenakshiUsername;

    @Value("${meenakshi.password}")
    private String meenakshiPassword;

    @Value("${kaviya.username}")
    private String kaviyaUsername;

    @Value("${kaviya.password}")
    private String kaviyaPassword;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // ── Public — no login needed ──
                        .requestMatchers(
                                "/",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ── Kaviya: Users & Roles only ──
                        .requestMatchers(
                                "/users-ui",
                                "/users-ui/**",
                                "/api/v1/users/**",
                                "/api/v1/roles/**"
                        ).hasRole("USERS")

                        // ── Meenakshi: Notifications only ──
                        .requestMatchers(
                                "/notifications-ui",
                                "/notifications-ui/**",
                                "/api/v1/notifications/**"
                        ).hasRole("NOTIFICATION")

                        // ── Everything else needs login ──
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", false)  // ← false means go to where they came from
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

        return http.build();

    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(

                // Meenakshi — can only access /notifications-ui
                User.withUsername(meenakshiUsername)
                        .password(passwordEncoder.encode(meenakshiPassword))
                        .roles("NOTIFICATION")
                        .build(),

                // Kaviya — can only access /users-ui
                User.withUsername(kaviyaUsername)
                        .password(passwordEncoder.encode(kaviyaPassword))
                        .roles("USERS")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}