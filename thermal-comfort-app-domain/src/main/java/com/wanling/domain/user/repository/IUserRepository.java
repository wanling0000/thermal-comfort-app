package com.wanling.domain.user.repository;

import java.util.Optional;

import com.wanling.domain.user.model.entity.UserEntity;

public interface IUserRepository {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    void save(UserEntity user);
    Optional<UserEntity> findById(String userId);
}
