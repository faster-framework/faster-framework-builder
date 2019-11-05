package cn.org.faster.framework.builder.modules.context;

import cn.org.faster.framework.builder.common.context.BuildContext;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminWeb.*;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public class AdminWebContext extends BuildContext {

    public AdminWebContext(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    protected void initBuildStrategy(List<BuildStrategy> strategyList) {
        strategyList.add(new ProjectFrameworkStrategy(builderModel));
        strategyList.add(new PageAddStrategy(builderModel));
        strategyList.add(new PageEditStrategy(builderModel));
        strategyList.add(new PageIndexStrategy(builderModel));
        strategyList.add(new RouterStrategy(builderModel));
        strategyList.add(new DefaultSettingsStrategy(builderModel));
    }
}
