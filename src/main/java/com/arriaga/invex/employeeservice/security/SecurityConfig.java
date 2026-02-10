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

  private static final String SCOPE_EMPLOYEE_READ = "SCOPE_employee.read";
  private static final String SCOPE_EMPLOYEE_WRITE = "SCOPE_employee.write";
  private static final String EMPLOYEES_WILDCARD = "/employees/*";

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.ignoringRequestMatchers(
            new AntPathRequestMatcher("/actuator/**"),
            new AntPathRequestMatcher("/auth/**"),
            new AntPathRequestMatcher("/employees/**"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/swagger-ui/**")))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(new AntPathRequestMatcher("/actuator/health", "GET")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**", "GET")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**", "GET")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/auth/token", "POST")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/employees", "GET")).hasAuthority(SCOPE_EMPLOYEE_READ)
            .requestMatchers(new AntPathRequestMatcher(EMPLOYEES_WILDCARD, "GET")).hasAuthority(SCOPE_EMPLOYEE_READ)
            .requestMatchers(new AntPathRequestMatcher("/employees/search", "GET")).hasAuthority(SCOPE_EMPLOYEE_READ)
            .requestMatchers(new AntPathRequestMatcher("/employees", "POST"))
            .hasAuthority(SCOPE_EMPLOYEE_WRITE)
            .requestMatchers(new AntPathRequestMatcher(EMPLOYEES_WILDCARD, "PUT"))
            .hasAuthority(SCOPE_EMPLOYEE_WRITE)
            .requestMatchers(new AntPathRequestMatcher(EMPLOYEES_WILDCARD, "DELETE"))
            .hasAuthority(SCOPE_EMPLOYEE_WRITE)
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }
}
