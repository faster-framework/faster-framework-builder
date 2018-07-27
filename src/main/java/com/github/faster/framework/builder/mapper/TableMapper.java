package com.github.faster.framework.builder.mapper;

import com.github.faster.framework.builder.entity.TableEntity;
import com.github.faster.framework.builder.model.TableColumnModel;
import com.github.faster.framework.core.mybatis.mapper.BaseMapper;

import java.util.List;

public interface TableMapper extends BaseMapper<TableEntity> {
    /**
     * 查询携带列的数据
     * @param query 条件
     * @return 携带列
     */
    List<TableColumnModel> selectWithColumn(TableEntity query);
}
