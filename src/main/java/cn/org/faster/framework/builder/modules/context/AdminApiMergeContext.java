package cn.org.faster.framework.builder.modules.context;

import cn.org.faster.framework.builder.common.context.BuildContext;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.common.strategy.adapter.AdminApiStrategyAdapter;
import cn.org.faster.framework.builder.modules.strategy.adminApi.*;
import cn.org.faster.framework.builder.modules.strategy.adminApiMerge.AdminControllerStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApiMerge.ApplicationYmlStrategy;
import cn.org.faster.framework.builder.modules.strategy.java.ApplicationEnvYmlStrategy;
import cn.org.faster.framework.builder.modules.strategy.java.GitIgnoreStrategy;
import cn.org.faster.framework.builder.modules.strategy.java.SpringBootApplicationStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台管理、接口合一
 * @author zhangbowen
 * @since 2018/12/14
 */
public class AdminApiMergeContext extends BuildContext {

    public AdminApiMergeContext(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    protected void initBuildStrategy(List<BuildStrategy> strategyList) {

        List<BuildStrategy> apiStrategyAdapters = new ArrayList<>();

        apiStrategyAdapters.add(new cn.org.faster.framework.builder.modules.strategy.api.EntityStrategy(builderModel));
        apiStrategyAdapters.add(new cn.org.faster.framework.builder.modules.strategy.api.MapperStrategy(builderModel));
        apiStrategyAdapters.add(new cn.org.faster.framework.builder.modules.strategy.api.RequestStrategy(builderModel));
        apiStrategyAdapters.add(new cn.org.faster.framework.builder.modules.strategy.api.ServiceStrategy(builderModel));
        apiStrategyAdapters.add(new cn.org.faster.framework.builder.modules.strategy.api.ControllerStrategy(builderModel));
        apiStrategyAdapters.add(new cn.org.faster.framework.builder.modules.strategy.api.TestStrategy(builderModel));

        apiStrategyAdapters.forEach(item->{
            item.setModulesName("api");
        });

        //创建admin策略
        List<BuildStrategy> adminApiStrategyAdapters = new ArrayList<>();

        adminApiStrategyAdapters.add(new ProjectFrameworkStrategy(builderModel));
        adminApiStrategyAdapters.add(new EntityStrategy(builderModel));
        adminApiStrategyAdapters.add(new ApplicationEnvYmlStrategy(builderModel));
        adminApiStrategyAdapters.add(new GitIgnoreStrategy(builderModel));
        adminApiStrategyAdapters.add(new MapperStrategy(builderModel));
        adminApiStrategyAdapters.add(new RequestStrategy(builderModel));
        adminApiStrategyAdapters.add(new ServiceStrategy(builderModel));
        adminApiStrategyAdapters.add(new SpringBootApplicationStrategy(builderModel));
        adminApiStrategyAdapters.add(new TestStrategy(builderModel));
        adminApiStrategyAdapters.add(new PomStrategy(builderModel));
        adminApiStrategyAdapters.add(new BaseTestStrategy(builderModel));
        adminApiStrategyAdapters.add(new ApplicationYmlStrategy(builderModel));
        adminApiStrategyAdapters.add(new ControllerStrategy(builderModel));

        adminApiStrategyAdapters.forEach(item->{
            item.setModulesName("admin");
        });

        strategyList.addAll(apiStrategyAdapters);
        strategyList.addAll(adminApiStrategyAdapters);


    }
}
