package cn.org.faster.framework.builder.modules.context;

import cn.org.faster.framework.builder.common.context.BuildContext;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApi.*;
import cn.org.faster.framework.builder.modules.strategy.java.ApplicationEnvYmlStrategy;
import cn.org.faster.framework.builder.modules.strategy.java.GitIgnoreStrategy;
import cn.org.faster.framework.builder.modules.strategy.java.SpringBootApplicationStrategy;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public class AdminApiContext extends BuildContext {

    public AdminApiContext(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    protected void initBuildStrategy(List<BuildStrategy> strategyList) {
        strategyList.add(new ProjectFrameworkStrategy(builderModel));
        strategyList.add(new EntityStrategy(builderModel));
        strategyList.add(new ApplicationEnvYmlStrategy(builderModel));
        strategyList.add(new GitIgnoreStrategy(builderModel));
        strategyList.add(new MapperStrategy(builderModel));
        strategyList.add(new RequestStrategy(builderModel));
        strategyList.add(new ServiceStrategy(builderModel));
        strategyList.add(new SpringBootApplicationStrategy(builderModel));
        strategyList.add(new TestStrategy(builderModel));
        strategyList.add(new PomStrategy(builderModel));
        strategyList.add(new BaseTestStrategy(builderModel));
        strategyList.add(new ApplicationYmlStrategy(builderModel));
        strategyList.add(new ControllerStrategy(builderModel));
    }
}
