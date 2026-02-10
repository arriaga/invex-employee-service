package com.arriaga.invex.employeeservice.security;

import com.arriaga.invex.employeeservice.api.dto.TokenRequest;
import com.arriaga.invex.employeeservice.api.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Profile("dev")
@Tag(name = "Authentication", description = "Dev-only token utilities")
public class DevTokenController {

  private final DevTokenService tokenService;

  public DevTokenController(DevTokenService tokenService) {
    this.tokenService = tokenService;
  }

  @PostMapping("/token")
  @Operation(summary = "Issue dev token", description = "Returns a local JWT for testing")
  @SecurityRequirements
  @ApiResponse(responseCode = "200", description = "Token issued",
      content = @Content(schema = @Schema(implementation = TokenResponse.class)))
  @ApiResponse(responseCode = "422", description = "Validation error")
  public TokenResponse issueToken(@Valid @RequestBody TokenRequest request) {
    return tokenService.issueToken(request);
  }
}
