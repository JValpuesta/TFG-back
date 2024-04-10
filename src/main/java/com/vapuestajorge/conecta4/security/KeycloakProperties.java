package com.vapuestajorge.conecta4.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String serverUrl;
    private String realmMaster;
    private String adminCli;
    private String realmName;
    private String consoleUser;
    private String consolePassword;
    private String scope;
    private String clientId;
    private String clientSecret;
    private String authorizationGrantType;
    private String tokenUri;
    private String certsUri;
}