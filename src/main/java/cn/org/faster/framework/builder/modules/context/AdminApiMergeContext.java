package cn.org.faster.framework.builder.modules.context;

import cn.org.faster.framework.builder.common.context.BuildContext;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApi.BaseTestStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApi.PomStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApi.ProjectFrameworkStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApiMerge.*;
import cn.org.faster.framework.builder.modules.strategy.java.ApplicationEnvYmlStrategy;
import cn.org.faster.framework.builder.modules.strategy.java.GitIgnoreStrategy;
import cn.org.faster.framework.builder.modules.strategy.java.SpringBootApplicationStrategy;

import java.util.List;

/**
 * 后台管理、接口合一
 *
 * @author zhangbowen
 * @since 2018/12/14
 */
public class AdminApiMergeContext extends BuildContext {

    public AdminApiMergeContext(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    protected void initBuildStrategy(List<BuildStrategy> strategyList) {
        String adminModuleName = "admin";
        String apiModuleName = "api.v1";


        //创建api策略
        strategyList.add(new EntityStrategy(builderModel, apiModuleName));
        strategyList.add(new MapperStrategy(builderModel, apiModuleName));
        strategyList.add(new ServiceStrategy(builderModel, apiModuleName));
        strategyList.add(new RequestStrategy(builderModel, apiModuleName));
        strategyList.add(new ApiControllerStrategy(builderModel, apiModuleName));


        //创建admin策略
        strategyList.add(new EntityStrategy(builderModel, adminModuleName));
        strategyList.add(new MapperStrategy(builderModel, adminModuleName));
        strategyList.add(new ServiceStrategy(builderModel, adminModuleName));
        strategyList.add(new RequestStrategy(builderModel, adminModuleName));
        strategyList.add(new AdminControllerStrategy(builderModel, adminModuleName));



        //公共策略
        strategyList.add(new ProjectFrameworkStrategy(builderModel));
        strategyList.add(new PomStrategy(builderModel));
        strategyList.add(new GitIgnoreStrategy(builderModel));
        strategyList.add(new ApplicationYmlStrategy(builderModel));
        strategyList.add(new ApplicationEnvYmlStrategy(builderModel));
        strategyList.add(new SpringBootApplicationStrategy(builderModel));
        strategyList.add(new BaseTestStrategy(builderModel));
        strategyList.add(new AdminTestStrategy(builderModel));
        strategyList.add(new ApiTestStrategy(builderModel));
    }
}
