package com.vapuestajorge.conecta4.keycloak.service.claims;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakContext implements KeycloakContextPort {

  /**
   * Get the email from the context if it's different from null, else return an ANONYMOUS_USER. If
   * getting claims it's null, an ANONYMOUS_USER is returned.
   *
   * @return UserEmail from context.
   */
  @Override
  public String getContextUserEmail() {
    try {
      Map<String, Object> claimsMap = getClaimsMap();
      String email = (String) claimsMap.get("email");
      return email == null ? "anonymousUser" : email;
    } catch (Exception e) {
      return "anonymousUser";
    }
  }

  /**
   * Get the email from the context if it's different from null, else return an ANONYMOUS_USER.
   *
   * @return Boolean if the tenantId is in the claims.
   */
  @Override
  public List<String> getUserTenants() {
    try {
      Map<String, Object> claimsMap = getClaimsMap();
      return (List<String>) claimsMap.get("tenants");
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  /**
   * Get the email from the context if it's different from null, else return an ANONYMOUS_USER.
   *
   * @param tenantId TenantId to verify.
   * @return Boolean if the tenantId is in the claims.
   */
  @Override
  public Boolean verifyIdTenant(String tenantId) {
    try {
      Map<String, Object> claimsMap = getClaimsMap();
      List<String> tenants = (List<String>) claimsMap.get("tenants");
      return tenants.contains(tenantId);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Get the user id from the context if it's different from null, else return an ANONYMOUS_USER.
   *
   * @return Boolean if the tenantId is in the claims.
   */
  @Override
  public String getUserId() {
    try {
      Map<String, Object> claimsMap = getClaimsMap();
      return (String) claimsMap.get("sub");
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public String getUserBearer() {
    return getToken().getBearerToken();
  }

  public Token getToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Jwt token = ((JwtAuthenticationToken) authentication).getToken();
    return new Token(token.getTokenValue());
  }

  /**
   * Method used to get the claims map from the context.
   *
   * @return Map with the claims.
   * @throws NoSuchFieldException If the field is not found.
   * @throws IllegalAccessException If the field is not accessible.
   */
  private Map<String, Object> getClaimsMap() throws NoSuchFieldException, IllegalAccessException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object credentials = authentication.getCredentials();
    Field claims = credentials.getClass().getDeclaredField("claims");
    ReflectionUtils.makeAccessible(claims);
    return (Map<String, Object>) claims.get(credentials);
  }
}
