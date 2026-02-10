package com.arriaga.invex.employeeservice.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class JwtConfigTest {

  private static final String VALID_SECRET = "0123456789abcdef0123456789abcdef";

  @Test
  void buildsJwtDecoderAndEncoderWithValidSecret() {
    JwtConfig config = new JwtConfig();

    assertThat(config.jwtDecoder(VALID_SECRET)).isNotNull();
    assertThat(config.jwtEncoder(VALID_SECRET)).isNotNull();
  }

  @Test
  void rejectsShortSecret() {
    JwtConfig config = new JwtConfig();

    assertThatThrownBy(() -> config.jwtDecoder("short"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("JWT secret must be at least 32 bytes");
  }
}
