package com.valpuestajorge.conecta4.user.service;

import com.valpuestajorge.conecta4.user.business.AppUser;
import com.valpuestajorge.conecta4.user.repository.UserRepository;
import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repo;

    @Override
    public Mono<AppUser> getByLogin(String login) throws NotFoundException {
        return this.repo.findUserByLogin(login);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<AppUser> post(AppUser entity) throws UnprocessableEntityException, NotFoundException {
        return repo.save(entity);
    }
}