package cn.org.faster.framework.builder.modules.context;

import cn.org.faster.framework.builder.common.context.BuildContext;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApi.AdminApiApplicationYmlStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApi.AdminApiBaseTestStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApi.AdminApiControllerStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApi.AdminApiPomStrategy;
import cn.org.faster.framework.builder.modules.strategy.java.*;

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
        strategyList.add(new EntityStrategy(builderModel));
        strategyList.add(new ApplicationEnvYmlStrategy(builderModel));
        strategyList.add(new GitIgnoreStrategy(builderModel));
        strategyList.add(new MapperStrategy(builderModel));
        strategyList.add(new RequestStrategy(builderModel));
        strategyList.add(new ServiceStrategy(builderModel));
        strategyList.add(new SpringBootApplicationStrategy(builderModel));
        strategyList.add(new TestStrategy(builderModel));
        strategyList.add(new AdminApiPomStrategy(builderModel));
        strategyList.add(new AdminApiBaseTestStrategy(builderModel));
        strategyList.add(new AdminApiApplicationYmlStrategy(builderModel));
        strategyList.add(new AdminApiControllerStrategy(builderModel));
    }
}
