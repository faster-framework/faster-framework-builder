package cn.org.faster.framework.builder.modules.context;

import cn.org.faster.framework.builder.common.context.BuildContext;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApi.ApplicationYmlStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApi.PomStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminApi.ProjectFrameworkStrategy;
import cn.org.faster.framework.builder.modules.strategy.java.ApplicationEnvYmlStrategy;
import cn.org.faster.framework.builder.modules.strategy.java.GitIgnoreStrategy;
import cn.org.faster.framework.builder.modules.strategy.java.SpringBootApplicationStrategy;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public class CmsApiContext extends BuildContext {

    public CmsApiContext(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    protected void initBuildStrategy(List<BuildStrategy> strategyList) {
        strategyList.add(new ProjectFrameworkStrategy(builderModel));
        strategyList.add(new PomStrategy(builderModel));
    }
}
