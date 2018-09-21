package cn.org.faster.framework.builder.model;

import cn.org.faster.framework.builder.model.TableColumnModel;
import lombok.Data;

import java.util.List;

/**
 * 生成器所需model
 */
@Data
public class BuilderModel {
    /**
     * 数据库主机名
     */
    private String dbHost;
    /**
     * 数据库端口号
     */
    private String dbPort;
    /**
     * 数据库名称
     */
    private String dbName;
    /**
     * 数据库用户
     */
    private String dbUsername;
    /**
     * 数据库密码
     */
    private String dbPwd;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 包路径 xxx.xx.xx
     */
    private String basePackagePath;
    /**
     * 根路径，由包路径转换为xxx/xx/xx/xx
     */
    private String basePath;
    /**
     * 依赖版本
     */
    private String dependencyVersion;

    /**
     * 依赖下载地址
     */
    private String dependencyUrl;

    /**
     * 表以及列数据
     */
    private List<TableColumnModel> tableColumnList;
}
