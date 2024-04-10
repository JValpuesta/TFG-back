package com.vapuestajorge.conecta4.keycloak.service.claims;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token {
  private String accessToken;
  private String refreshToken;
  private Integer expiresIn;
  private Integer refreshExpiresIn;
  private String tokenType;
  private String sessionState;
  private String scope;

  public Token(String token) {
    this.accessToken = token;
  }

  public String getBearerToken() {
    return "Bearer %s".formatted(this.getAccessToken());
  }
}
