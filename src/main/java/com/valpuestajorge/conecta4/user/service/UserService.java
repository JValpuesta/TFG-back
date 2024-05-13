package com.valpuestajorge.conecta4.user.service;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import com.valpuestajorge.conecta4.shared.restapibusiness.service.BusinessServicePort;
import com.valpuestajorge.conecta4.user.business.AppUser;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface UserService extends BusinessServicePort<AppUser> {

    Mono<AppUser> getByLogin(String login) throws NotFoundException;

    Mono<AppUser> post(AppUser entity) throws UnprocessableEntityException, NotFoundException;
}
