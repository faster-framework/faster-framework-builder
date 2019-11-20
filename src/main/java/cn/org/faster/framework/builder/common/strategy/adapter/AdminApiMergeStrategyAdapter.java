package cn.org.faster.framework.builder.common.strategy.adapter;

import cn.org.faster.framework.builder.common.constants.BuilderConstants;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.common.utils.BuilderUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AdminApiMergeStrategyAdapter extends BuildStrategy {
    protected String basePackage;
    protected String modulesName;

    public AdminApiMergeStrategyAdapter(BuilderModel builderModel, String modulesName) {
        super(builderModel);
        this.modulesName = modulesName;
        srcPath = BuilderConstants.JAVA_PATH + builderModel.getBasePath() + "/" + BuilderUtils.packagePathToPath(modulesName) + "/";
        basePackage = builderModel.getBasePackagePath() + "." + modulesName;
    }
}
