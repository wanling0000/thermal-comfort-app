package com.wanling.infrastructure.persistent.mapper;

import com.wanling.infrastructure.persistent.po.ComfortSummaryReports;

/**
* @author fwl
* @description 针对表【comfort_summary_reports】的数据库操作Mapper
* @createDate 2025-07-30 20:57:57
* @Entity com.wanling.infrastructure.persistent.po.ComfortSummaryReports
*/
public interface ComfortSummaryReportsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ComfortSummaryReports record);

    int insertSelective(ComfortSummaryReports record);

    ComfortSummaryReports selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ComfortSummaryReports record);

    int updateByPrimaryKey(ComfortSummaryReports record);

}
