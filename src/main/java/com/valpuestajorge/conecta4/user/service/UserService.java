package com.valpuestajorge.conecta4.user.service;

import com.valpuestajorge.conecta4.user.business.AppUser;
import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public interface UserService {

    Mono<AppUser> getByLogin(String login) throws NotFoundException;

    Mono<AppUser> post(AppUser entity) throws UnprocessableEntityException, NotFoundException;
}
