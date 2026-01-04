package cn.org.faster.framework.builder.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangbowen
 * @since 2018/10/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DependencyModel {
    private String groupId;
    private String artifactId;
}
