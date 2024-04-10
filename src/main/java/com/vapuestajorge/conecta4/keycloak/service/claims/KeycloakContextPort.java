package com.vapuestajorge.conecta4.keycloak.service.claims;

import java.util.List;

public interface KeycloakContextPort {
  String getContextUserEmail();

  List<String> getUserTenants();

  Boolean verifyIdTenant(String tenantId);

  String getUserId();

  String getUserBearer();
}
