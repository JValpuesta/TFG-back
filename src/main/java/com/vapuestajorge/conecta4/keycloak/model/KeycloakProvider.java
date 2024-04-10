package com.vapuestajorge.conecta4.keycloak.model;

import com.vapuestajorge.conecta4.keycloak.exception.CreateTrustAllContextException;
import com.vapuestajorge.conecta4.security.KeycloakProperties;
import lombok.AllArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Locale;

@Component
@AllArgsConstructor
public class KeycloakProvider {
    private static final String DEFAULT_PROFILE = "default";
    private static final String PROD_PROFILE_NAME = "prod";
    private final Environment environment;
    private final MessageSource messageSource;

    private String getProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            return DEFAULT_PROFILE;
        }
        return activeProfiles[0];
    }

    public RealmResource getRealmResource(KeycloakProperties keycloakProperties) {
        SSLContext sslContext = createTrustAllSslContext();
        HostnameVerifier allHostsValid = (hostname, session) -> true;
        Keycloak keycloak;
        if (getProfile().equals(PROD_PROFILE_NAME)) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakProperties.getServerUrl())
                    .realm(keycloakProperties.getRealmMaster())
                    .clientId(keycloakProperties.getAdminCli())
                    .username(keycloakProperties.getConsoleUser())
                    .password(keycloakProperties.getConsolePassword())
                    .clientSecret(keycloakProperties.getClientSecret())
                    .resteasyClient(new ResteasyClientBuilderImpl()
                            .connectionPoolSize(10)
                            .build())
                    .build();
        } else {
            ResteasyClient client = ((ResteasyClientBuilder) ResteasyClientBuilder.newBuilder())
                    .sslContext(sslContext)
                    .hostnameVerifier(allHostsValid)
                    .connectionPoolSize(10)
                    .build();
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakProperties.getServerUrl())
                    .realm(keycloakProperties.getRealmMaster())
                    .clientId(keycloakProperties.getAdminCli())
                    .username(keycloakProperties.getConsoleUser())
                    .password(keycloakProperties.getConsolePassword())
                    .clientSecret(keycloakProperties.getClientSecret())
                    .resteasyClient(client)
                    .build();
        }
        return keycloak.realm(keycloakProperties.getRealmName());
    }

    private SSLContext createTrustAllSslContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            String createTrustAllSslContext = messageSource.getMessage("KeycloakProvider.createTrustAllSslContext", null, locale);

            throw new CreateTrustAllContextException(createTrustAllSslContext, e);
        }
    }

    public UsersResource getUserResource(KeycloakProperties keycloakProperties) {
        RealmResource realmResource = getRealmResource(keycloakProperties);
        return realmResource.users();
    }

}
