package com.arriaga.invex.employeeservice.security;

import com.arriaga.invex.employeeservice.api.dto.TokenRequest;
import com.arriaga.invex.employeeservice.api.dto.TokenResponse;
import javax.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Profile("dev")
public class DevTokenController {

  private final DevTokenService tokenService;

  public DevTokenController(DevTokenService tokenService) {
    this.tokenService = tokenService;
  }

  @PostMapping("/token")
  public TokenResponse issueToken(@Valid @RequestBody TokenRequest request) {
    return tokenService.issueToken(request);
  }
}
