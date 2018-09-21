package cn.org.faster.framework.builder.engine.adminPermission;

import cn.org.faster.framework.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionModel extends BaseEntity {
    private String name;
    private String code;
    private Long parentId;
    private String parentIds;
}
