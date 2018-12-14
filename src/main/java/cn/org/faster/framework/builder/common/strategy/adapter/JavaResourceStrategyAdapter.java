package cn.org.faster.framework.builder.common.strategy.adapter;

import cn.org.faster.framework.builder.common.constants.BuilderConstants;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public abstract class JavaResourceStrategyAdapter extends BuildStrategy {
    protected String basePath;

    public JavaResourceStrategyAdapter(BuilderModel builderModel) {
        super(builderModel);
        this.basePath = BuilderConstants.JAVA_RESOURCES_PATH;
    }
}
