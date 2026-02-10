package com.arriaga.invex.employeeservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(new AntPathRequestMatcher("/actuator/health", "GET")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**", "GET")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**", "GET")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/employees", "GET")).hasAuthority("SCOPE_employee.read")
            .requestMatchers(new AntPathRequestMatcher("/employees/*", "GET")).hasAuthority("SCOPE_employee.read")
            .requestMatchers(new AntPathRequestMatcher("/employees/search", "GET")).hasAuthority("SCOPE_employee.read")
            .requestMatchers(new AntPathRequestMatcher("/employees", "POST"))
            .hasAuthority("SCOPE_employee.write")
            .requestMatchers(new AntPathRequestMatcher("/employees/*", "PUT"))
            .hasAuthority("SCOPE_employee.write")
            .requestMatchers(new AntPathRequestMatcher("/employees/*", "DELETE"))
            .hasAuthority("SCOPE_employee.write")
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }
}
