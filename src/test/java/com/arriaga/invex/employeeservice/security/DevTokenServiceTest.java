package com.arriaga.invex.employeeservice.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.arriaga.invex.employeeservice.api.dto.TokenRequest;
import com.arriaga.invex.employeeservice.api.dto.TokenResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

@ExtendWith(MockitoExtension.class)
class DevTokenServiceTest {

  @Mock
  private JwtEncoder jwtEncoder;

  private DevTokenService service;

  @BeforeEach
  void setUp() {
    service = new DevTokenService(jwtEncoder);
  }

  @Test
  void issueTokenUsesDefaultExpirationAndNormalizesScope() {
    TokenRequest request = new TokenRequest();
    request.setSubject("tester");
    request.setScopes(List.of(" employee.read ", " ", "employee.write"));

    Jwt jwt = Jwt.withTokenValue("token")
        .header("alg", "HS256")
        .claim("sub", "tester")
        .build();

    when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

    TokenResponse response = service.issueToken(request);

    assertThat(response.getAccessToken()).isEqualTo("token");
    assertThat(response.getTokenType()).isEqualTo("Bearer");
    assertThat(response.getExpiresInMinutes()).isEqualTo(60);
    assertThat(response.getScope()).isEqualTo("employee.read employee.write");
  }

  @Test
  void issueTokenUsesProvidedExpiration() {
    TokenRequest request = new TokenRequest();
    request.setSubject("tester");
    request.setScopes(List.of("employee.read"));
    request.setExpiresInMinutes(15);

    Jwt jwt = Jwt.withTokenValue("token")
        .header("alg", "HS256")
        .claim("sub", "tester")
        .build();

    when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

    TokenResponse response = service.issueToken(request);

    assertThat(response.getExpiresInMinutes()).isEqualTo(15);
  }
}
