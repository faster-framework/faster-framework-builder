package cn.org.faster.framework.builder.common.strategy.adapter;

import cn.org.faster.framework.builder.common.constants.BuilderConstants;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import com.alibaba.druid.filter.config.ConfigTools;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public abstract class JavaStrategyAdapter extends BuildStrategy {
    protected String basePath;
    protected String basePackage;

    public JavaStrategyAdapter(BuilderModel builderModel) {
        super(builderModel);
        basePath = BuilderConstants.JAVA_PATH + builderModel.getBasePath() + "/modules/v1/";
        basePackage = builderModel.getBasePackagePath() + ".modules.v1";
        builderModel.setDependencyVersion(builderModel.getDependencyVersion().replace("v", ""));
        try {
            String[] arr = ConfigTools.genKeyPair(512);
            builderModel.setDbEncryptPwd(ConfigTools.encrypt(arr[0], builderModel.getDbPwd()));
            builderModel.setDbPublicKey(arr[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
