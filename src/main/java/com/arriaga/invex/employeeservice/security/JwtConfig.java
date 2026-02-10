package com.arriaga.invex.employeeservice.security;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {

  @Bean
  public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.secret-key}") String secret) {
    SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(key).build();
  }
}
