package com.vapuestajorge.conecta4.keycloak.service.claims;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakRole {
  private String id;
  private String name;
  private String description;
  private Boolean clientRole = false;
  private Boolean composite = false;
  private String containerId;
  private Object attributes;
  private List<String> roleIds;
  private List<KeycloakRole> roles;

  public KeycloakRole(String name, String scope) {
    this.setName("ROLE_%s%s".formatted(name.toUpperCase(), scope.toUpperCase()));
    this.setClientRole(false);
    this.setComposite(false);
  }

  public KeycloakRole(String roleName) {
    this.setName(roleName);
    this.setClientRole(false);
    this.setComposite(false);
  }
}
