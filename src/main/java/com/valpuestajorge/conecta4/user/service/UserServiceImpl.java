package com.valpuestajorge.conecta4.user.service;

import com.valpuestajorge.conecta4.security.encoder.PasswordEncoderWrapper;
import com.valpuestajorge.conecta4.shared.patch_compare.PatchComparePort;
import com.valpuestajorge.conecta4.shared.restapibusiness.service.BusinessService;
import com.valpuestajorge.conecta4.user.business.AppUser;
import com.valpuestajorge.conecta4.user.repository.UserRepository;
import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Getter
public class UserServiceImpl extends BusinessService<AppUser> implements UserService {

    private final UserRepository repo;

    private final PatchComparePort patchComparePort;

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