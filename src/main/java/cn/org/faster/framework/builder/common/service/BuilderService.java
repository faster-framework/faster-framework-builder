package cn.org.faster.framework.builder.common.service;

import cn.org.faster.framework.builder.common.config.BuilderProperties;
import cn.org.faster.framework.builder.common.config.ProjectOption;
import cn.org.faster.framework.builder.common.constants.BuilderConstants;
import cn.org.faster.framework.builder.common.context.GeneratedArchive;
import cn.org.faster.framework.builder.common.exception.BuilderException;
import cn.org.faster.framework.builder.common.metadata.MetadataReader;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.model.ColumnModel;
import cn.org.faster.framework.builder.common.model.TableColumnModel;
import cn.org.faster.framework.builder.common.model.request.BuilderRequest;
import cn.org.faster.framework.builder.common.model.request.DatabaseRequest;
import cn.org.faster.framework.builder.common.model.request.DependencyRequest;
import cn.org.faster.framework.builder.common.utils.BuilderUtils;
import cn.org.faster.framework.builder.modules.context.AdminApiContext;
import cn.org.faster.framework.builder.modules.context.AdminApiMergeContext;
import cn.org.faster.framework.builder.modules.context.AdminWebContext;
import cn.org.faster.framework.builder.modules.context.ApiContext;
import cn.org.faster.framework.builder.modules.context.CmsApiContext;
import cn.org.faster.framework.builder.modules.context.CmsWebContext;
import cn.org.faster.framework.builder.modules.context.DbContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 生成服务：读取元数据 → 补全模型 → 选择 Context 产出 ZIP。
 *
 * <p>不再注入 {@code HttpServletResponse}：Service 只返回数据，由 Controller 决定响应形态。</p>
 *
 * @author zhangbowen
 */
@Service
public class BuilderService {

    private static final Logger log = LoggerFactory.getLogger(BuilderService.class);

    /**
     * 必须连接数据库才能生成的项目类型。
     */
    private static final Set<Integer> TYPES_REQUIRING_DATABASE = Set.of(
            BuilderConstants.API, BuilderConstants.ADMIN_API,
            BuilderConstants.ADMIN_WEB, BuilderConstants.ADMIN_API_MERGE);

    /**
     * 必须提供根包路径的项目类型。
     */
    private static final Set<Integer> TYPES_REQUIRING_PACKAGE = Set.of(
            BuilderConstants.API, BuilderConstants.ADMIN_API,
            BuilderConstants.ADMIN_API_MERGE, BuilderConstants.CMS_API);

    /**
     * 需要下载官方骨架的项目类型。
     */
    private static final Set<Integer> TYPES_REQUIRING_SKELETON = Set.of(
            BuilderConstants.ADMIN_API, BuilderConstants.ADMIN_WEB,
            BuilderConstants.ADMIN_API_MERGE, BuilderConstants.CMS_API, BuilderConstants.CMS_WEB);

    private final MetadataReader metadataReader;
    private final BuilderProperties properties;

    public BuilderService(MetadataReader metadataReader, BuilderProperties properties) {
        this.metadataReader = metadataReader;
        this.properties = properties;
    }

    /**
     * 各生成类型可选的版本与骨架地址。
     *
     * @return 配置列表
     */
    public List<ProjectOption> options() {
        return properties.projects();
    }

    /**
     * 生成项目压缩包。
     *
     * @param request 请求参数
     * @return 内存中的 ZIP + 文件名
     */
    public GeneratedArchive build(BuilderRequest request) {
        validateRequest(request);
        List<TableColumnModel> tableColumnModelList = readTables(request);
        BuilderModel builderModel = initBuildModel(request, tableColumnModelList);
        byte[] content = process(request.type(), builderModel);
        log.info("生成完成: type={}, project={}, 表数={}, 大小={}KB",
                request.type(), request.business().projectName(), tableColumnModelList.size(), content.length / 1024);
        return new GeneratedArchive(content, request.business().projectName() + ".zip");
    }

    private void validateRequest(BuilderRequest request) {
        int type = request.type();
        if (TYPES_REQUIRING_DATABASE.contains(type)) {
            DatabaseRequest database = request.database();
            if (database == null || BuilderUtils.isBlank(database.host())) {
                throw new BuilderException(HttpStatus.BAD_REQUEST, "当前生成类型需要数据库连接信息");
            }
        }
        if (TYPES_REQUIRING_PACKAGE.contains(type) && BuilderUtils.isBlank(request.business().basePackagePath())) {
            throw new BuilderException(HttpStatus.BAD_REQUEST, "当前生成类型需要填写根包路径");
        }
        if (TYPES_REQUIRING_SKELETON.contains(type)) {
            ProjectOption option = properties.projectOf(type);
            boolean requestUrlBlank = request.dependency() == null || BuilderUtils.isBlank(request.dependency().url());
            if (requestUrlBlank && (option == null || BuilderUtils.isBlank(option.url()))) {
                throw new BuilderException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "当前生成类型未配置官方骨架地址，请设置 builder.projects 中 type=" + type + " 的 url");
            }
        }
    }

    private List<TableColumnModel> readTables(BuilderRequest request) {
        if (!TYPES_REQUIRING_DATABASE.contains(request.type())) {
            return List.of();
        }
        String tableName = request.business().tableName();
        return metadataReader.read(request.database(), BuilderUtils.isBlank(tableName) ? null : tableName);
    }

    private byte[] process(int type, BuilderModel builderModel) {
        try {
            return switch (type) {
                case BuilderConstants.API -> new ApiContext(builderModel).process();
                case BuilderConstants.ADMIN_API -> new AdminApiContext(builderModel).process();
                case BuilderConstants.ADMIN_WEB -> new AdminWebContext(builderModel).process();
                case BuilderConstants.ADMIN_API_MERGE -> new AdminApiMergeContext(builderModel).process();
                case BuilderConstants.SQL -> new DbContext(builderModel).process();
                case BuilderConstants.CMS_API -> new CmsApiContext(builderModel).process();
                case BuilderConstants.CMS_WEB -> new CmsWebContext(builderModel).process();
                default -> throw new BuilderException(HttpStatus.BAD_REQUEST, "不支持的生成类型: " + type);
            };
        } catch (IOException ex) {
            throw new BuilderException(HttpStatus.INTERNAL_SERVER_ERROR, "生成 ZIP 失败：" + ex.getMessage(), ex);
        }
    }

    /**
     * @param columnList 列
     * @return 完善列信息
     */
    private List<ColumnModel> completeColumn(List<ColumnModel> columnList) {
        return columnList.stream().peek(item -> {
            item.setColumnComment(cleanComment(item.getColumnComment()));
            item.setColumnNameHump(BuilderUtils.hump(item.getColumnName()));
            item.setColumnNameHumpUpFirst(BuilderUtils.firstCharToUpperCase(item.getColumnNameHump()));
            item.setJavaType(BuilderUtils.convertToJavaType(item.getDataType()));
            item.setJavaImportType(BuilderUtils.javaImportType(item.getJavaType()));
        }).collect(Collectors.toList());
    }

    /**
     * @param tableColumnList 要生成的表以及列的数据
     * @return 完善 tableColumn 中所需参数
     */
    private List<TableColumnModel> completeTableColumn(List<TableColumnModel> tableColumnList) {
        return tableColumnList.stream().peek(item -> {
            item.setBusinessEnName(BuilderUtils.delPrefixAndHump(item.getTableName()));
            item.setBusinessEnNameUpFirst(BuilderUtils.firstCharToUpperCase(item.getBusinessEnName()));
            item.setBusinessCnName(BuilderUtils.isBlank(item.getTableComment())
                    ? item.getBusinessEnName()
                    : BuilderUtils.filterBusinessCnName(item.getTableComment()));
            item.setColumnList(completeColumn(item.getColumnList()));
        }).collect(Collectors.toList());
    }

    private String cleanComment(String comment) {
        return comment == null ? "" : comment.replaceAll("\\s", "");
    }

    /**
     * 初始化
     *
     * @param request         请求参数
     * @param tableColumnList 表、列
     */
    private BuilderModel initBuildModel(BuilderRequest request, List<TableColumnModel> tableColumnList) {
        BuilderModel builderParam = new BuilderModel();
        DatabaseRequest database = request.database();
        if (database != null) {
            builderParam.setDbHost(database.host());
            builderParam.setDbPort(database.port());
            builderParam.setDbName(database.name());
            builderParam.setDbPwd(database.password());
            builderParam.setDbUsername(database.username());
        }
        builderParam.setProjectName(request.business().projectName());
        builderParam.setTableColumnList(completeTableColumn(tableColumnList));
        builderParam.setBasePackagePath(request.business().basePackagePath());
        builderParam.setBasePath(BuilderUtils.packagePathToPath(request.business().basePackagePath()));
        builderParam.setJwtBase64Secret(properties.jwtBase64Secret());
        builderParam.setJwtEnv(properties.jwtEnv());
        builderParam.setSkeletonMaxBytes(properties.skeletonMaxBytes());
        builderParam.setSkeletonTimeoutMillis(properties.skeletonTimeout().toMillis());
        applyDependency(builderParam, request);
        return builderParam;
    }

    /**
     * 依赖版本与骨架地址：优先取请求显式传入的值，否则回落到服务端配置，避免前端硬编码导致漂移。
     */
    private void applyDependency(BuilderModel builderParam, BuilderRequest request) {
        ProjectOption option = properties.projectOf(request.type());
        DependencyRequest dependency = request.dependency();
        String version = dependency != null && BuilderUtils.isNotBlank(dependency.version())
                ? dependency.version()
                : option == null ? null : option.version();
        String url = dependency != null && BuilderUtils.isNotBlank(dependency.url())
                ? dependency.url()
                : option == null ? null : option.url();
        builderParam.setDependencyVersion(version);
        builderParam.setDependencyUrl(BuilderUtils.isBlank(url) ? null : url);
    }
}
