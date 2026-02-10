package com.arriaga.invex.employeeservice.security;

import com.arriaga.invex.employeeservice.api.dto.TokenRequest;
import com.arriaga.invex.employeeservice.api.dto.TokenResponse;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DevTokenService {

  private static final int DEFAULT_EXPIRATION_MINUTES = 60;

  private final JwtEncoder jwtEncoder;

  public DevTokenService(JwtEncoder jwtEncoder) {
    this.jwtEncoder = jwtEncoder;
  }

  public TokenResponse issueToken(TokenRequest request) {
    int expiresIn = resolveExpiration(request.getExpiresInMinutes());
    String scope = normalizeScope(request.getScopes());
    Instant now = Instant.now();
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .subject(request.getSubject())
        .issuedAt(now)
        .expiresAt(now.plusSeconds(expiresIn * 60L))
        .claim("scope", scope)
        .build();

    JwsHeader headers = JwsHeader.with(MacAlgorithm.HS256).build();
    String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(headers, claims)).getTokenValue();

    TokenResponse response = new TokenResponse();
    response.setAccessToken(tokenValue);
    response.setTokenType("Bearer");
    response.setExpiresInMinutes(expiresIn);
    response.setScope(scope);
    return response;
  }

  private int resolveExpiration(Integer requested) {
    if (requested == null || requested <= 0) {
      return DEFAULT_EXPIRATION_MINUTES;
    }
    return requested;
  }

  private String normalizeScope(List<String> scopes) {
    return scopes.stream()
        .map(String::trim)
        .filter(scope -> !scope.isBlank())
        .collect(Collectors.joining(" "));
  }
}
