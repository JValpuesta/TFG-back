package com.valpuestajorge.conecta4.app_user.repository;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AppUserRepository extends ReactiveCrudRepository<AppUser,Long> {
}