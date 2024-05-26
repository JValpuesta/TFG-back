package com.valpuestajorge.conecta4.security.service;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import com.valpuestajorge.conecta4.security.exception.InvalidLoginException;
import com.valpuestajorge.conecta4.security.model.ChangePasswordDto;
import com.valpuestajorge.conecta4.security.model.LoginOutputDto;
import com.valpuestajorge.conecta4.user.entity.business.AppUser;
import reactor.core.publisher.Mono;

public interface ISecurityService {

    Mono<AppUser> searchUserByUsername(String username);
    Mono<LoginOutputDto> login(String username, String password) throws NotFoundException, UnprocessableEntityException;
    Mono<Void> changePassword(ChangePasswordDto changePasswordDto) throws InvalidLoginException, NotFoundException, UnprocessableEntityException;
    void lostPassword(String username) throws NotFoundException, UnprocessableEntityException;
    Mono<Void> setRandomPassword(String user) throws NotFoundException, UnprocessableEntityException;
    Mono<Void> updateUserAvailability(String user, Boolean isAvailable) throws NotFoundException, UnprocessableEntityException;
}


