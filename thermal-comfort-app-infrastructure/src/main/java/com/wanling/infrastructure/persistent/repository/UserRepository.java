package com.wanling.infrastructure.persistent.repository;

import java.util.Optional;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wanling.domain.user.model.entity.UserEntity;
import com.wanling.domain.user.repository.IUserRepository;
import com.wanling.infrastructure.persistent.mapper.UsersMapper;
import com.wanling.infrastructure.persistent.po.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository implements IUserRepository {
    private final UsersMapper usersMapper;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        Optional<Users> user = usersMapper.findByUsername(username);
        return user.map(this::toEntity);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return usersMapper.findByEmail(email).map(this::toEntity);
    }

    @Override
    public void save(UserEntity user) {
        Users po = new Users();
        po.setUserId(UUID.randomUUID().toString());
        po.setName(user.name());
        po.setEmail(user.email());
        po.setPassword(user.password());
        po.setCreatedAt(user.createdAt());
        usersMapper.insert(po);
    }

    @Override
    public Optional<UserEntity> findById(String userId) {
        Users po = usersMapper.selectById(userId);
        return Optional.ofNullable(po).map(this::toEntity);
    }

    private UserEntity toEntity(Users po) {
        log.info("🛠 Mapping Users to UserEntity: po.created_at = {}", po.getCreatedAt());

        return new UserEntity(
                po.getUserId(),
                po.getName(),
                po.getEmail(),
                po.getPassword(),
                po.getCreatedAt()
        );
    }
}
