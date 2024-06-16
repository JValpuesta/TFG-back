package com.valpuestajorge.conecta4.shared.restapibusiness.repository;

import com.valpuestajorge.conecta4.shared.restapibusiness.entity.persistence.BusinessEntity;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

@NoRepositoryBean
public interface BusinessReactiveRepository<P extends BusinessEntity> extends ReactiveCrudRepository<P, Long> {
}
