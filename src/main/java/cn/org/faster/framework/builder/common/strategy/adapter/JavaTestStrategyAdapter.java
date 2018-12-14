package cn.org.faster.framework.builder.common.strategy.adapter;

import cn.org.faster.framework.builder.common.constants.BuilderConstants;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public abstract class JavaTestStrategyAdapter extends BuildStrategy {
    protected String basePath;
    protected String basePackage;

    public JavaTestStrategyAdapter(BuilderModel builderModel) {
        super(builderModel);
        basePath = BuilderConstants.JAVA_TEST_PATH + builderModel.getBasePath() + "/modules/v1/";
        basePackage = builderModel.getBasePackagePath() + ".modules.v1";
    }
}
