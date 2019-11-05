package cn.org.faster.framework.builder.modules.context;

import cn.org.faster.framework.builder.common.context.BuildContext;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.modules.strategy.db.BaseDbStrategy;
import cn.org.faster.framework.builder.modules.strategy.db.PermissionStrategy;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public class DbContext extends BuildContext {

    public DbContext(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    protected void initBuildStrategy(List<BuildStrategy> strategyList) {
        strategyList.add(new PermissionStrategy(builderModel));
        strategyList.add(new BaseDbStrategy(builderModel));
    }
}
