package com.wanling.infrastructure.persistent.mapper;

import com.wanling.infrastructure.persistent.po.Users;

/**
* @author fwl
* @description 针对表【users】的数据库操作Mapper
* @createDate 2025-05-05 22:10:48
* @Entity com.wanling.infrastructure.persistent.po.Users
*/
public interface UsersMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

}
