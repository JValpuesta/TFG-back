package com.valpuestajorge.conecta4.security.service;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import com.valpuestajorge.conecta4.security.exception.InvalidLoginException;
import com.valpuestajorge.conecta4.security.model.ChangePasswordDto;
import com.valpuestajorge.conecta4.security.model.LoginOutputDto;
import com.valpuestajorge.conecta4.user.business.AppUser;
import reactor.core.publisher.Mono;

public interface ISecurityService {

    Mono<AppUser> searchUserByUsername(String username);
    LoginOutputDto login(String username, String password) throws NotFoundException, UnprocessableEntityException;
    void changePassword(ChangePasswordDto changePasswordDto) throws InvalidLoginException, NotFoundException, UnprocessableEntityException;
    void lostPassword(String username) throws NotFoundException, UnprocessableEntityException;
    void setRandomPassword(String user) throws NotFoundException, UnprocessableEntityException;
    void updateUserAvailability(String user, Boolean isAvailable) throws NotFoundException, UnprocessableEntityException;
}


