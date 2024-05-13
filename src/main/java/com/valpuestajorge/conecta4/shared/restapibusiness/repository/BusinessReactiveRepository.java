package com.valpuestajorge.conecta4.shared.restapibusiness.repository;

import com.valpuestajorge.conecta4.shared.restapibusiness.persistance.BusinessEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessReactiveRepository<P extends BusinessEntity> extends ReactiveCrudRepository<P, Long> {
}