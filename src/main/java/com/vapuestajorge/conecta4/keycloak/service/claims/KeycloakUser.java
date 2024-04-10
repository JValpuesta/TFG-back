package com.vapuestajorge.conecta4.keycloak.service.claims;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakUser {
  private String id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String type;
  private String password;
  private Boolean emailVerified;
  private Boolean enabled;
  private Boolean temporary;
  private Map<String, Object> attributes;
  private List<String> roleIds;
  private List<KeycloakRole> roles;

  public void setDefaultProperties() {
    this.temporary = false;
    this.type = "password";
    this.setEmailVerified(false);
    this.setEnabled(true);
  }

  public void addRoles(List<KeycloakRole> keycloakRoles) {
    if (this.roles == null) this.roles = new ArrayList<>();
    this.roles.addAll(keycloakRoles);
  }
}
