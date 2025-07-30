package com.wanling.infrastructure.persistent.mapper;

import com.wanling.infrastructure.persistent.po.AiInteractionLogs;

/**
* @author fwl
* @description 针对表【ai_interaction_logs】的数据库操作Mapper
* @createDate 2025-07-30 20:57:57
* @Entity com.wanling.infrastructure.persistent.po.AiInteractionLogs
*/
public interface AiInteractionLogsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(AiInteractionLogs record);

    int insertSelective(AiInteractionLogs record);

    AiInteractionLogs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AiInteractionLogs record);

    int updateByPrimaryKey(AiInteractionLogs record);

}
