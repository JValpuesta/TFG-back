package com.valpuestajorge.conecta4.user.service;

import com.valpuestajorge.conecta4.security.encoder.PasswordEncoderWrapper;
import com.valpuestajorge.conecta4.user.business.AppUser;
import com.valpuestajorge.conecta4.user.repository.UserRepository;
import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repo;

    private final PasswordEncoderWrapper passwordEncoder;

    @Override
    public Mono<AppUser> getByLogin(String login) throws NotFoundException {
        return this.repo.findUserByLogin(login);
    }

    @Override
    public Mono<AppUser> post(AppUser entity) throws UnprocessableEntityException, NotFoundException {
        this.setPassword(entity);
        return repo.save(entity);
    }

    private void setPassword(AppUser original) {
        original.setPassword(passwordEncoder.cypherPassword(original.getUsername(), original.getPassword()));
    }
}