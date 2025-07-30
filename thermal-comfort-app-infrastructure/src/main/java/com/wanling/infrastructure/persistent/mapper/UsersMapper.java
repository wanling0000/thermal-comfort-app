package com.wanling.infrastructure.persistent.mapper;

import java.util.Optional;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanling.infrastructure.persistent.po.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersMapper extends BaseMapper<Users> {

    int deleteByPrimaryKey(Long id);

    int insert(Users record);

    int insertSelective(Users record);


    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
    Optional<Users> findByUsername(@Param("username") String username);
    Optional<Users> findByEmail(@Param("email") String email);
    Optional<Users> findById(String userId);
}
