package cn.org.faster.framework.builder.common.strategy.adapter;

import cn.org.faster.framework.builder.common.constants.BuilderConstants;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AdminApiStrategyAdapter extends BuildStrategy {
    protected String basePackage;

    public AdminApiStrategyAdapter(BuilderModel builderModel) {
        super(builderModel);
        srcPath = BuilderConstants.JAVA_PATH + builderModel.getBasePath() + "/modules/";
        basePackage = builderModel.getBasePackagePath() + ".modules";
    }
}
