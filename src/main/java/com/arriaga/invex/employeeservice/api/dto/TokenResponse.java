package com.arriaga.invex.employeeservice.api.dto;

public class TokenResponse {

  private String accessToken;
  private String tokenType;
  private Integer expiresInMinutes;
  private String scope;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public Integer getExpiresInMinutes() {
    return expiresInMinutes;
  }

  public void setExpiresInMinutes(Integer expiresInMinutes) {
    this.expiresInMinutes = expiresInMinutes;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }
}
