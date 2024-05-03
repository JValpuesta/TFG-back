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
    public LoginOutputDto login(String username, String password) throws NotFoundException, UnprocessableEntityException {
        Authentication auth = this.authenticate(username, password);
        if (Objects.nonNull(auth)) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            return jwtUtils.getToken(auth);
        } else {
            Locale locale = LocaleContextHolder.getLocale();
            String message = messageSource.getMessage("user.unsuccessfulLogin.message", null, locale);
            return new LoginOutputDto("", "", "", "", "", HttpStatus.UNAUTHORIZED, message);
        }
    }

    private Authentication authenticate(String username, String password) {
        AppUser appUser = userRepository.findUserByUsername(username).block();
        if (Objects.nonNull(appUser)) {
            if (Boolean.FALSE.equals(appUser.getAccountNotLocked())) {
                Locale locale = LocaleContextHolder.getLocale();
                String message = messageSource.getMessage("appUser.lockedAccount.message", null, locale);
                updateUserLoginData(appUser, LocalDateTime.now(), message);
                return null;
            }
            if (Boolean.FALSE.equals(appUser.getAccountNotExpired())) {
                Locale locale = LocaleContextHolder.getLocale();
                String message = messageSource.getMessage("appUser.expiredAccount.message", null, locale);
                updateUserLoginData(appUser, LocalDateTime.now(), message);
                return null;
            }
            if (Boolean.FALSE.equals(appUser.getCredentialNotExpired())) {
                Locale locale = LocaleContextHolder.getLocale();
                String message = messageSource.getMessage("appUser.expiredCredential.message", null, locale);
                updateUserLoginData(appUser, LocalDateTime.now(), message);
                return null;
            }
            if (Boolean.TRUE.equals(appUser.getRequiredPasswordChangeFlag())) {
                Locale locale = LocaleContextHolder.getLocale();
                String message = messageSource.getMessage("appUser.requiredPasswordChange.message", null, locale);
                updateUserLoginData(appUser, LocalDateTime.now(), message);
                return null;
            }
            if (passwordEncoder.matches(HashUtils.getHash(password + "_" + username), appUser.getPassword())) {
                String stringAuthorities = "ROLE_" + appUser.getUserRole();
                Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);
                updateUserLoginData(appUser, LocalDateTime.now(), null);
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            } else {
                Locale locale = LocaleContextHolder.getLocale();
                String message = messageSource.getMessage("appUser.unsuccessfulPassword.message", null, locale);
                updateUserLoginData(appUser, LocalDateTime.now(), message);
            }
        }
        return null;
    }

    private void updateUserLoginData(AppUser appUser, LocalDateTime lastLogin, String motiveFailedLogin) {
        appUser.setLastLogin(lastLogin);
        appUser.setMotiveFailedLogin(motiveFailedLogin);
        userRepository.save(appUser);
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) throws InvalidLoginException, NotFoundException, UnprocessableEntityException {
        AppUser appUser = userRepository.findUserByUsername(changePasswordDto.getUserName()).block();
        if (Objects.isNull(appUser)) {
            Locale locale = LocaleContextHolder.getLocale();
            String message = messageSource.getMessage("appUser.notFound.login.message", null, locale);
            throw new NotFoundException(message);
        }
        if (Boolean.TRUE.equals(appUser.getRequiredPasswordChangeFlag())) {
            if (changePasswordDto.getPreviousPassword().equals(appUser.getTemporaryPassword())) {
                appUser.setRequiredPasswordChangeFlag(false);
                appUser.setTemporaryPassword(null);
            }
        } else if (Objects.isNull(authenticate(changePasswordDto.getUserName(), changePasswordDto.getPreviousPassword()))) {
            Locale locale = LocaleContextHolder.getLocale();
            String message = messageSource.getMessage("appUser.unsuccessfulPassword.message", null, locale);
            throw new InvalidLoginException(message);
        }
        appUser.setPassword(passwordEncoder.cypherPassword(changePasswordDto.getUserName(), changePasswordDto.getNewPassword()));
        userRepository.save(appUser);

    }

    @Override
    public void lostPassword(String username) {
        setRandomPassword(username);
    }

    @Override
    public void setRandomPassword(String username) {
        String temporaryPassword = PasswordGenerator.generateRandomPassword(12);
        setUserTemporaryPassword(username, temporaryPassword);
    }

    private void setUserTemporaryPassword(String userName, String temporaryPassword) {
        AppUser satAppUser = userRepository.findUserByUsername(userName).block();
        if(Objects.nonNull(satAppUser)){
            satAppUser.setTemporaryPassword(temporaryPassword);
            satAppUser.setRequiredPasswordChangeFlag(true);
            userRepository.save(satAppUser);
        }
        //sendEmail(satAppUser.getEmail(), temporaryPassword);
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
    public void updateUserAvailability(String username, Boolean isAvailable) throws NotFoundException, UnprocessableEntityException {
        AppUser appUser = userRepository.findUserByUsername(username).block();
        if (Objects.isNull(appUser)) {
            Locale locale = LocaleContextHolder.getLocale();
            String message = messageSource.getMessage("appUser.notFound.login.message", null, locale);
            throw new NotFoundException(message);
        }
        appUser.setAccountNotLocked(isAvailable);
        userRepository.save(appUser);
    }

}
