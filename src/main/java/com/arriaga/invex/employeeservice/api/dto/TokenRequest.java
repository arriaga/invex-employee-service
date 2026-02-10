package com.arriaga.invex.employeeservice.api.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class TokenRequest {

  @NotBlank
  private String subject;

  @NotEmpty
  private List<@NotBlank String> scopes;

  private Integer expiresInMinutes;

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public List<String> getScopes() {
    return scopes;
  }

  public void setScopes(List<String> scopes) {
    this.scopes = scopes;
  }

  public Integer getExpiresInMinutes() {
    return expiresInMinutes;
  }

  public void setExpiresInMinutes(Integer expiresInMinutes) {
    this.expiresInMinutes = expiresInMinutes;
  }
}
