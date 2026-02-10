package com.arriaga.invex.employeeservice.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {

  @Bean
  public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.secret-key}") String secret) {
    SecretKeySpec key = new SecretKeySpec(secretBytes(secret), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(key).build();
  }

  @Bean
  @Profile("dev")
  public JwtEncoder jwtEncoder(@Value("${spring.security.oauth2.resourceserver.jwt.secret-key}") String secret) {
    SecretKey key = new SecretKeySpec(secretBytes(secret), "HmacSHA256");
    OctetSequenceKey jwk = new OctetSequenceKey.Builder(key)
        .algorithm(JWSAlgorithm.HS256)
        .keyID("dev-token-key")
        .build();
    return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
  }

  private byte[] secretBytes(String secret) {
    byte[] bytes = secret == null ? new byte[0] : secret.getBytes(StandardCharsets.UTF_8);
    if (bytes.length < 32) {
      throw new IllegalStateException(
          "JWT secret must be at least 32 bytes for HS256. "
              + "Set spring.security.oauth2.resourceserver.jwt.secret-key or JWT_SECRET.");
    }
    return bytes;
  }
}
