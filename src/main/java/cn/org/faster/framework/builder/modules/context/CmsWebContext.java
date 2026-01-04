package cn.org.faster.framework.builder.modules.context;

import cn.org.faster.framework.builder.common.context.BuildContext;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.modules.strategy.adminWeb.DefaultSettingsStrategy;
import cn.org.faster.framework.builder.modules.strategy.cmsWeb.ProjectFrameworkStrategy;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public class CmsWebContext extends BuildContext {

    public CmsWebContext(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    protected void initBuildStrategy(List<BuildStrategy> strategyList) {
        strategyList.add(new ProjectFrameworkStrategy(builderModel));
        strategyList.add(new DefaultSettingsStrategy(builderModel));
    }
}
