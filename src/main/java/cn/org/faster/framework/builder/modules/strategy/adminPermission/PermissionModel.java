package cn.org.faster.framework.builder.modules.strategy.adminPermission;

import cn.org.faster.framework.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionModel extends BaseEntity {
    private String name;
    private String code;
    private Long parentId;
    private String parentIds;
}
