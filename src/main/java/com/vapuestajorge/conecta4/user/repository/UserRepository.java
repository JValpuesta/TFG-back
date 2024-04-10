package com.vapuestajorge.conecta4.user.repository;

import com.vapuestajorge.conecta4.errors.NotFoundException;
import com.vapuestajorge.conecta4.user.business.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User,Integer> {

    User findUserByLogin(String login) throws NotFoundException;

    Boolean existsByLogin(String login);

}