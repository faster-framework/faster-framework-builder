package cn.org.faster.framework.builder.common.strategy.adapter;

import cn.org.faster.framework.builder.common.constants.BuilderConstants;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public abstract class ApiStrategyAdapter extends BuildStrategy {
    protected String basePackage;

    public ApiStrategyAdapter(BuilderModel builderModel) {
        super(builderModel);
        srcPath = BuilderConstants.JAVA_PATH + builderModel.getBasePath() + "/" + modulesName + "/";
        basePackage = builderModel.getBasePackagePath() + "." + modulesName;
    }
}
