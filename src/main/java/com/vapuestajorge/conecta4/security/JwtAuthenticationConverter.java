package com.vapuestajorge.conecta4.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Slf4j
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    public static final String CLAIM_RESOURCE_ACCESS = "resource_access";
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    @Value("${jwt.auth.converter.principle-attribute}")
    private String  principleAttributte;
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
         Collection<GrantedAuthority> authorities = Stream
                .concat(jwtGrantedAuthoritiesConverter.convert(source).stream(),extractResourceRoles(source).stream())
                .toList();
        return new JwtAuthenticationToken(source, authorities, getPrincipleName(source));
    }

    private String getPrincipleName(Jwt source) {
        String claimName = JwtClaimNames.SUB;
        if (principleAttributte!=null){
            claimName = principleAttributte;
        }
        log.debug("User with successful token access {}", (String) source.getClaim(claimName));
        return source.getClaim(claimName);
    }

    private Collection<? extends  GrantedAuthority> extractResourceRoles(Jwt source) {
        Map<String,Object> resourceAccess;
        Map<String,Object> resource;
        Collection<String> resourceRoles;
        List<GrantedAuthority> emptyList = List.of();
        if (source.getClaim(CLAIM_RESOURCE_ACCESS)==null){
            return emptyList;
        }
        resourceAccess = source.getClaim(CLAIM_RESOURCE_ACCESS);
        if(resourceAccess.get(resourceId)==null){
            return emptyList;
        }
        resource = (Map<String, Object>) resourceAccess.get(resourceId);
        if(resource.get("roles")==null){
            return emptyList;
        }
        resourceRoles = (Collection<String>) resource.get("roles");
        return mapTokenRolesToSpringSecurityRoles(resourceRoles);
    }

    private static List<SimpleGrantedAuthority> mapTokenRolesToSpringSecurityRoles(Collection<String> resourceRoles) {
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role)))
                .toList();
    }
}
