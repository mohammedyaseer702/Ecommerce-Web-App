package com.example.demo.config;

import com.example.demo.security.CustomAccessDeniedHandler;
import com.example.demo.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import com.example.demo.security.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity   // ✅ enables @PreAuthorize if you use it
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;


    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf(csrf -> csrf.disable())

        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .authorizeHttpRequests(auth -> auth

            // 🔓 Public APIs (login/register)
            .requestMatchers("/api/auth/**").permitAll()

            // 👑 Admin APIs
            .requestMatchers("/api/admin/**").hasRole("ADMIN")

            // 👤 User APIs
            .requestMatchers("/api/orders","/api/orders/**").hasAnyRole("USER","ADMIN")

            // everything else
            .anyRequest().authenticated()
        )

        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler)
        )

        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

}
