package cn.org.faster.framework.builder.modules.context;

import cn.org.faster.framework.builder.common.context.BuildContext;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.modules.strategy.EntityStrategy;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public class ApiContext extends BuildContext {

    public ApiContext(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void initBuildStrategy(List<BuildStrategy> strategyList) {
        strategyList.add(new EntityStrategy(builderModel));
    }
}
