package com.luigivis.srcownapigateway.repository;

import com.luigivis.srcownapigateway.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
