package com.vapuestajorge.conecta4.keycloak.service;

import com.vapuestajorge.conecta4.errors.NotFoundException;
import com.vapuestajorge.conecta4.errors.UnprocessableEntityException;
import com.vapuestajorge.conecta4.keycloak.exception.InvalidLoginException;
import com.vapuestajorge.conecta4.keycloak.exception.InvalidUserCreationException;
import com.vapuestajorge.conecta4.keycloak.exception.ServiceCertsAccessException;
import com.vapuestajorge.conecta4.keycloak.model.ChangePasswordDto;
import com.vapuestajorge.conecta4.keycloak.model.KeycloakProvider;
import com.vapuestajorge.conecta4.keycloak.model.LoginOutputDto;
import com.vapuestajorge.conecta4.security.KeycloakProperties;
import com.vapuestajorge.conecta4.shared.PatchComparePort;
import com.vapuestajorge.conecta4.user.business.User;
import com.vapuestajorge.conecta4.user.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLContext;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class KeycloakSecurityServiceImpl implements ISecurityService {
    public static final String PROD_PROFILE_NAME = "prod";
    public static final String DEFAULT_PROFILE = "default";
    private final UserRepository userRepositoryPort;
    private final PatchComparePort patchCompare;
    private final KeycloakProperties keycloakProperties;
    private final Environment environment;
    private final KeycloakProvider keycloakProvider;
    private final MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    @Override
    public List<UserRepresentation> findAllUsers() {
        return keycloakProvider
                .getRealmResource(keycloakProperties)
                .users()
                .list();
    }

    @Override
    public UserRepresentation searchUserByUsername(String username) {
        return keycloakProvider
                .getRealmResource(keycloakProperties)
                .users()
                .searchByUsername(username, true)
                .stream()
                .findFirst()
                .orElseThrow();
    }

    @Override
    public void createUser(@NonNull User user) throws InvalidUserCreationException {
        int status;
        UsersResource usersResource = keycloakProvider.getUserResource(keycloakProperties);
        UserRepresentation userRepresentation = mapUserRepresentation(user);
        userVerification(userRepresentation);
        Response response = usersResource.create(userRepresentation);
        status = response.getStatus();
        if (status == 201) {
            String userId = getUserId(response);
            setPassword(user.getPassword(), usersResource, userId);
            Set<String> roles = new HashSet<>();
            roles.add(user.getUserRole().getDescription());
            addRoles(roles, userId);
            log.info("User {} created successfully", user.getLogin());
        } else if (status == 409) {
            log.error("User {} already defined", user.getLogin());
        } else {
            String errorMessage = extractErrorMessage(response);
            log.error("Error creating user {}: {}, status: {}", user.getLogin(), errorMessage, status);
            String createUser = messageSource.getMessage("KeycloakSecurityServiceImpl.createUser", null, locale);
            throw new InvalidUserCreationException(String.format(createUser, user.getLogin()));
        }
    }

    private String extractErrorMessage(Response response) {
        try {
            if (response.hasEntity()) {
                return response.readEntity(String.class);
            }
            return "No error message provided";
        } catch (Exception e) {
            log.error("Error reading error message from response", e);
            return "Error reading error message";
        }
    }

    @Override
    public LoginOutputDto login(String username, String password) throws NotFoundException, UnprocessableEntityException {
        String profile = getProfile();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", keycloakProperties.getAuthorizationGrantType());
        body.add("client_id", keycloakProperties.getClientId());
        body.add("client_secret", keycloakProperties.getClientSecret());
        body.add("username", username);
        body.add("password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response;

        User user = null;
        Boolean exists = userRepositoryPort.existsByLogin(username);
        if (Boolean.TRUE.equals(exists)) {
            user = userRepositoryPort.findUserByLogin(username);
        }

        try {
            if (profile.equals(PROD_PROFILE_NAME)) {
                response = new RestTemplate().exchange(keycloakProperties.getTokenUri(),
                        HttpMethod.POST,
                        request,
                        String.class
                );
            } else {
                RestTemplate restTemplate = new RestTemplate(getRequestFactory());
                response = restTemplate.exchange(
                        keycloakProperties.getTokenUri(),
                        HttpMethod.POST,
                        request,
                        String.class
                );

            }
        } catch (HttpClientErrorException e) {
            if (Boolean.TRUE.equals(exists)) {
                String message = "User failed to log because: '" + e.getMessage() + "'.";
                updateUserLoginData(user, LocalDateTime.now(), message);
            }
            if (e.getMessage().startsWith("401")) {
                return new LoginOutputDto("", "", "", "", "", HttpStatus.UNAUTHORIZED, e.getMessage());
            }
            return new LoginOutputDto("", "", "", "", "", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            throw new InvalidLoginException(e.getMessage());
        }

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            Map<String, Object> result = getMap(response.getBody());
            user = updateUserLoginData(user, LocalDateTime.now(), null);
            String configurations = user.getConfigurations();
            return new LoginOutputDto(result.get("\"access_token\"").toString().replace("\"", ""),
                    result.get("\"expires_in\"").toString(),
                    result.get("\"refresh_token\"").toString().replace("\"", ""),
                    result.get("\"refresh_expires_in\"").toString(), configurations, HttpStatus.OK, "Successful login");
        } else {
            if (Boolean.TRUE.equals(exists)) {
                String message = "User failed to log because: '" + response.getBody() + "'.";
                updateUserLoginData(user, LocalDateTime.now(), message);
            }
            return new LoginOutputDto("", "", "", "", "", response.getStatusCode(), response.getBody());
        }

    }

    private User updateUserLoginData(User user, LocalDateTime lastLogin, String motiveFailedLogin) {

        user.setLastLogin(lastLogin);
        user.setMotiveFailedLogin(motiveFailedLogin);
        return userRepositoryPort.save(user).block();
    }

    private String getProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            return DEFAULT_PROFILE;
        }
        return activeProfiles[0];
    }

    private HttpComponentsClientHttpRequestFactory getRequestFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
        final SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        final BasicHttpClientConnectionManager connectionManager =
                new BasicHttpClientConnectionManager(socketFactoryRegistry);
        final CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    private Boolean validateUser(String username, String password) throws NotFoundException {
        User satUser = userRepositoryPort.findUserByLogin(username);
        if (Boolean.TRUE.equals(satUser.getRequiredPasswordChangeFlag()) && (password.equals(satUser.getTemporaryPassword()))) {
            return true;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", keycloakProperties.getAuthorizationGrantType());
        body.add("client_id", keycloakProperties.getClientId());
        body.add("client_secret", keycloakProperties.getClientSecret());
        body.add("username", username);
        body.add("password", password);


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = new RestTemplate().exchange(
                keycloakProperties.getTokenUri(),
                HttpMethod.POST,
                request,
                String.class
        );
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    private Map<String, Object> getMap(String body) {

        Map<String, Object> myMap = new LinkedHashMap<>();
        if (!StringUtils.hasText(body))
            return myMap;

        String s = body.replace("{", "").replace("}", "");

        String[] pairs = s.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            myMap.put(keyValue[0], keyValue[1]);
        }

        return myMap;
    }

    private void addRoles(Set<String> roles, String userId) {
        RealmResource realmResource = keycloakProvider.getRealmResource(keycloakProperties);
        List<RoleRepresentation> roleRepresentations;
        if (roles == null || roles.isEmpty()) {
            roleRepresentations = Collections.emptyList();
        } else {
            roleRepresentations = realmResource.roles()
                    .list()
                    .stream()
                    .filter(role -> roles
                            .stream()
                            .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                    .toList();
        }
        realmResource.users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(roleRepresentations);
    }

    private static void userVerification(UserRepresentation userRepresentation) {
        // TO-DO check production email verification
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
    }

    private static UserRepresentation mapUserRepresentation(User user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(user.getName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setUsername(user.getLogin());
        return userRepresentation;
    }

    private static void setPassword(String password, UsersResource usersResource, String userId) {
        CredentialRepresentation credentialRepresentation = passwordInitialization();
        credentialRepresentation.setValue(password);
        usersResource.get(userId).resetPassword(credentialRepresentation);
    }

    private static void setTemporaryPassword(String password, UsersResource usersResource, String userId) {
        CredentialRepresentation credentialRepresentation = passwordInitialization();
        credentialRepresentation.setTemporary(true);
        credentialRepresentation.setValue(password);
        usersResource.get(userId).resetPassword(credentialRepresentation);
    }

    private static CredentialRepresentation passwordInitialization() {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        return credentialRepresentation;
    }

    private static String getUserId(Response response) {
        String path = response.getLocation().getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }

    @Override
    public void deleteUser(String userId) {
        keycloakProvider.getUserResource(keycloakProperties).get(userId).remove();
    }

    @Override
    public void updateUser(String userId, @NonNull User userDto) throws IllegalAccessException, NotFoundException, ParseException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        UserRepresentation userRepresentation = mapUserRepresentation(userDto);
        userVerification(userRepresentation);
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            CredentialRepresentation credentialRepresentation = passwordInitialization();
            credentialRepresentation.setValue(userDto.getPassword());
            userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        }
        UserResource userResource = keycloakProvider.getUserResource(keycloakProperties).get(userId);
        userResource.update(userRepresentation);
        Map<String, Object> mapValues = patchCompare.getMapFromInput(userDto);

        User userGeneric = userRepositoryPort.findUserByLogin(userResource.toRepresentation().getUsername());
        userGeneric = patchCompare.getPatchCompare(mapValues, userGeneric);
        userRepositoryPort.save(userGeneric);
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) throws InvalidLoginException, NotFoundException {
        boolean isValid = validateUser(changePasswordDto.getUserName(), changePasswordDto.getPreviousPassword());
        if (isValid) {
            UserRepresentation userRepresentation = searchUserByUsername(changePasswordDto.getUserName());
            UsersResource usersResource = keycloakProvider.getUserResource(keycloakProperties);
            setPassword(changePasswordDto.getNewPassword(), usersResource, userRepresentation.getId());
            setUserTemporaryPassword(changePasswordDto.getUserName(), "");
        } else {
            String invalidLoginException = messageSource.getMessage("KeycloakSecurityServiceImpl.InvalidLoginException", null, locale);
            throw new InvalidLoginException(String.format(invalidLoginException, changePasswordDto.getUserName()));
        }
    }

    private void setUserTemporaryPassword(String userName, String temporaryPassword) throws NotFoundException {
        Boolean requiredPasswordChange = !temporaryPassword.isEmpty();
        User satUser = userRepositoryPort.findUserByLogin(userName);
        satUser.setTemporaryPassword(temporaryPassword);
        satUser.setRequiredPasswordChangeFlag(requiredPasswordChange);
        userRepositoryPort.save(satUser);
    }

    @Override
    public void lostPassword(String userName) throws NotFoundException {
        setRandomPassword(userName);

    }

    @Override
    public void setRandomPassword(String user) throws NotFoundException {
        UsersResource usersResource = keycloakProvider.getUserResource(keycloakProperties);
        UserRepresentation userRepresentation = searchUserByUsername(user);
        String temporaryPassword = PasswordGenerator.generateRandomPassword(12);
        setTemporaryPassword(temporaryPassword, usersResource, userRepresentation.getId());
        setUserTemporaryPassword(user, temporaryPassword);
    }

    @Override
    public void updateUserAvailability(String user, Boolean isAvailable) throws NotFoundException {
        User satUser = userRepositoryPort.findUserByLogin(user);
        satUser.setUserAvailableFlag(isAvailable);
        userRepositoryPort.save(satUser);
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(isAvailable);
        UserResource userResource = keycloakProvider.getUserResource(keycloakProperties).get(user);
        userResource.update(userRepresentation);
    }

    @Override
    public void setRole(String userName, String role) {
        UserRepresentation userRepresentation = searchUserByUsername(userName);
        Set<String> roles = new HashSet<>();
        roles.add(role);
        addRoles(roles, userRepresentation.getId());
    }

    @Override
    public void createRole(String name) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setClientRole(true);
        roleRepresentation.setName(name);
        RealmResource realmResource = keycloakProvider.getRealmResource(keycloakProperties);
        realmResource.roles().create(roleRepresentation);
    }

    @Override
    public List<String> getRoles(String username) {
        UserRepresentation userRepresentation = searchUserByUsername(username);
        return userRepresentation.getRealmRoles();
    }

    @Override
    public ResponseEntity<String> getCerts() {
        String profile = getProfile();
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response;
        try {
            if (profile.equals(PROD_PROFILE_NAME)) {
                response = new RestTemplate().exchange(keycloakProperties.getCertsUri(),
                        HttpMethod.GET,
                        request,
                        String.class
                );
            } else {
                RestTemplate restTemplate = new RestTemplate(getRequestFactory());
                response = restTemplate.exchange(
                        keycloakProperties.getCertsUri(),
                        HttpMethod.GET,
                        request,
                        String.class
                );

            }
        } catch (HttpClientErrorException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new ServiceCertsAccessException(e);
        }
        return response;
    }
}
