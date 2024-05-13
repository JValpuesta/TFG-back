package com.valpuestajorge.conecta4.security.jwt.service;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import com.valpuestajorge.conecta4.security.encoder.HashUtils;
import com.valpuestajorge.conecta4.security.encoder.PasswordEncoderWrapper;
import com.valpuestajorge.conecta4.security.exception.InvalidLoginException;
import com.valpuestajorge.conecta4.security.model.ChangePasswordDto;
import com.valpuestajorge.conecta4.security.model.LoginOutputDto;
import com.valpuestajorge.conecta4.security.service.ISecurityService;
import com.valpuestajorge.conecta4.security.service.PasswordGenerator;
import com.valpuestajorge.conecta4.user.business.AppUser;
import com.valpuestajorge.conecta4.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Service
public class InternalJWTSecurityService implements ISecurityService {
    private final JWTUtils jwtUtils;
    private UserRepository userRepository;
    private final MessageSource messageSource;
    private final PasswordEncoderWrapper passwordEncoder;
    //private final EmailService emailService;
    //private final SatParameterRepositoryPort parameterPort;

    //private static final Long ID_MAIL_RANDOM_PASSWORD_PARAMETER = 1L;

    @Override
    public Mono<AppUser> searchUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public Mono<LoginOutputDto> login(String username, String password) {
        return authenticate(username, password)
                .flatMap(auth -> {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    return Mono.just(jwtUtils.getToken(auth));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    Locale locale = LocaleContextHolder.getLocale();
                    String message = messageSource.getMessage("user.unsuccessfulLogin.message", null, locale);
                    return Mono.just(new LoginOutputDto("", "", "", "", "", HttpStatus.UNAUTHORIZED, message));
                }));
    }

    public Mono<Authentication> authenticate(String username, String password) {
        return userRepository.findUserByUsername(username)
                .flatMap(appUser -> {
                    if (Objects.isNull(appUser)) {
                        return Mono.empty();
                    }
                    if (Boolean.FALSE.equals(appUser.getAccountNotLocked())) {
                        return createAndLogFailure(appUser, "user.lockedAccount.message");
                    }
                    if (Boolean.FALSE.equals(appUser.getAccountNotExpired())) {
                        return createAndLogFailure(appUser, "user.expiredAccount.message");
                    }
                    if (Boolean.FALSE.equals(appUser.getCredentialNotExpired())) {
                        return createAndLogFailure(appUser, "user.expiredCredential.message");
                    }
                    if (Boolean.TRUE.equals(appUser.getRequiredPasswordChangeFlag())) {
                        return createAndLogFailure(appUser, "user.requiredPasswordChange.message");
                    }
                    if (passwordEncoder.matches(HashUtils.getHash(password + "_" + username), appUser.getPassword())) {
                        String stringAuthorities = "ROLE_" + appUser.getUserRole();
                        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);
                        updateUserLoginData(appUser, null, null);
                        return Mono.just(new UsernamePasswordAuthenticationToken(username, password, authorities));
                    } else {
                        return createAndLogFailure(appUser, "user.unsuccessfulPassword.message");
                    }
                });
    }

    private Mono<Authentication> createAndLogFailure(AppUser appUser, String messageCode) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(messageCode, null, locale);
        updateUserLoginData(appUser, null, message);
        return Mono.empty();
    }

    public Mono<Void> updateUserLoginData(AppUser appUser, LocalDateTime lastLogin, String motiveFailedLogin) {
        appUser.setLastLogin(lastLogin);
        appUser.setMotiveFailedLogin(motiveFailedLogin);
        return userRepository.save(appUser).then();
    }

    @Override
    public Mono<Void> changePassword(ChangePasswordDto changePasswordDto) {
        return userRepository.findUserByUsername(changePasswordDto.getUserName())
                .flatMap(appUser -> {
                    if (Objects.isNull(appUser)) {
                        Locale locale = LocaleContextHolder.getLocale();
                        String message = messageSource.getMessage("user.notFound.login.message", null, locale);
                        return Mono.error(new NotFoundException(message));
                    }

                    if (Boolean.TRUE.equals(appUser.getRequiredPasswordChangeFlag())) {
                        if (changePasswordDto.getPreviousPassword().equals(appUser.getTemporaryPassword())) {
                            appUser.setRequiredPasswordChangeFlag(false);
                            appUser.setTemporaryPassword(null);
                        }
                    } else if (Objects.isNull(authenticate(changePasswordDto.getUserName(), changePasswordDto.getPreviousPassword()))) {
                        Locale locale = LocaleContextHolder.getLocale();
                        String message = messageSource.getMessage("user.unsuccessfulPassword.message", null, locale);
                        return Mono.error(new InvalidLoginException(message));
                    }

                    appUser.setPassword(passwordEncoder.cypherPassword(changePasswordDto.getUserName(), changePasswordDto.getNewPassword()));
                    return userRepository.save(appUser).then();
                });
    }

    @Override
    public void lostPassword(String username) {
        setRandomPassword(username);
    }

    @Override
    public Mono<Void> setRandomPassword(String username) {
        String temporaryPassword = PasswordGenerator.generateRandomPassword(12);
        return setUserTemporaryPassword(username, temporaryPassword);
    }

    private Mono<Void> setUserTemporaryPassword(String userName, String temporaryPassword) {
        return userRepository.findUserByUsername(userName)
                .flatMap(user -> {
                    if (Objects.nonNull(user)) {
                        user.setTemporaryPassword(temporaryPassword);
                        user.setRequiredPasswordChangeFlag(true);
                        return userRepository.save(user).then();
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /*private void sendEmail(String email, String temporaryPassword) {
        SatParameter emailDefinition = parameterPort.getById(ID_MAIL_RANDOM_PASSWORD_PARAMETER);
        if (Objects.isNull(emailDefinition)) {
            String subject = "Clave cambiada";
            String body = "<BR>Ante su solicitud, esta es la nueva clave generada: <BR>";
            body += temporaryPassword;
            body += "<BR> Este es un correo enviado automáticamente, por favor no responder a esta dirección.<P>Autostock Nuvex";
            emailService.sendEmail(email, subject, body);
        } else {
            JsonNode jsonMail = emailDefinition.getValue();
            String subject = jsonMail.get("subject").asText();
            String body = jsonMail.get("body").asText();
            body += temporaryPassword;
            emailService.sendEmail(email, subject, body);
        }
    }*/

    @Override
    public Mono<Void> updateUserAvailability(String username, Boolean isAvailable) {
        return userRepository.findUserByUsername(username)
                .flatMap(user -> {
                    if (Objects.isNull(user)) {
                        Locale locale = LocaleContextHolder.getLocale();
                        String message = messageSource.getMessage("user.notFound.login.message", null, locale);
                        return Mono.error(new NotFoundException(message));
                    }
                    user.setAccountNotLocked(isAvailable);
                    return userRepository.save(user).then();
                });
    }

}
