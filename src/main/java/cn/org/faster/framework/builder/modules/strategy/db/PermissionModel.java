package cn.org.faster.framework.builder.modules.strategy.db;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 权限记录，用于渲染 {@code permission.sql.ftl}。
 *
 * <p>替代原先对框架 {@code BaseEntity} 的继承：只保留模板实际使用到的字段，
 * 并原地实现 {@code preInsert} 的语义（主键、审计时间、逻辑删除标记、排序）。</p>
 *
 * @author zhangbowen
 */
@Data
public class PermissionModel {
    private Long id;
    private String name;
    private String code;
    private Long parentId;
    private String parentIds;
    private Long createBy;
    private Long updateBy;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer sort;
    private String remark;
    private Integer deleted;

    /**
     * 插入前补全：生成主键与审计字段。
     */
    public void preInsert() {
        this.id = cn.org.faster.framework.builder.common.id.IdGenerator.next();
        LocalDateTime now = LocalDateTime.now();
        this.createDate = now;
        this.updateDate = now;
        this.createBy = 0L;
        this.updateBy = 0L;
        this.deleted = 0;
        if (this.sort == null) {
            this.sort = 0;
        }
    }
}
