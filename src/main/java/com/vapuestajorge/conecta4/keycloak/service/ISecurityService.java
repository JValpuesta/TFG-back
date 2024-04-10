package com.vapuestajorge.conecta4.keycloak.service;

import com.vapuestajorge.conecta4.errors.NotFoundException;
import com.vapuestajorge.conecta4.errors.UnprocessableEntityException;
import com.vapuestajorge.conecta4.keycloak.exception.InvalidLoginException;
import com.vapuestajorge.conecta4.keycloak.exception.InvalidUserCreationException;
import com.vapuestajorge.conecta4.keycloak.model.ChangePasswordDto;
import com.vapuestajorge.conecta4.keycloak.model.LoginOutputDto;
import com.vapuestajorge.conecta4.user.business.User;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

public interface ISecurityService {

    List<UserRepresentation> findAllUsers();
    UserRepresentation searchUserByUsername(String username);
    void createUser(User user) throws NotFoundException, InvalidUserCreationException;
    LoginOutputDto login(String username, String password) throws NotFoundException, UnprocessableEntityException;
    void deleteUser(String userId);
    void updateUser(String userId, User userDto) throws IllegalAccessException, NotFoundException, ParseException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException;
    void changePassword(ChangePasswordDto changePasswordDto) throws InvalidLoginException, NotFoundException;
    void lostPassword(String username) throws NotFoundException;
    void setRandomPassword(String user) throws NotFoundException;
    void updateUserAvailability(String user, Boolean isAvailable) throws NotFoundException;
    void setRole(String userName, String role);
    void createRole(String name);
    List<String> getRoles(String username);
    ResponseEntity<String> getCerts();
}


