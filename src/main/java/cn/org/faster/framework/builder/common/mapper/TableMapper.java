package cn.org.faster.framework.builder.common.mapper;

import cn.org.faster.framework.builder.common.entity.TableEntity;
import cn.org.faster.framework.builder.common.model.TableColumnModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface TableMapper extends BaseMapper<TableEntity> {
    /**
     * 查询携带列的数据
     * @param query 条件
     * @return 携带列
     */
    List<TableColumnModel> selectWithColumn(TableEntity query);
}
