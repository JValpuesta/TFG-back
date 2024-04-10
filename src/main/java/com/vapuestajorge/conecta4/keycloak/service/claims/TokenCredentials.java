package com.vapuestajorge.conecta4.keycloak.service.claims;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenCredentials extends KeycloakUser {
  private String grantType;
  private String clientId;
  private String clientSecret;

  public void initialize(KeycloakUser keycloakUser, String clientId, String clientSecret) {
    super.setUsername(keycloakUser.getUsername());
    super.setPassword(keycloakUser.getPassword());
    this.grantType = "password";
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  public Map<String, String> initializeMapParams() {
    return Map.of(
        "grant_type", this.grantType,
        "client_id", this.clientId,
        "username", super.getUsername(),
        "password", super.getPassword(),
        "client_secret", this.clientSecret);
  }
}
