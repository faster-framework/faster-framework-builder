package cn.org.faster.framework.builder.common.strategy.adapter;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;

/**
 * @author zhangbowen
 * @since 2018/12/17
 */
public abstract class WebStrategyAdapter extends BuildStrategy {
    public WebStrategyAdapter(BuilderModel builderModel) {
        super(builderModel);
        basePath = "/src/modules/";
    }
}
