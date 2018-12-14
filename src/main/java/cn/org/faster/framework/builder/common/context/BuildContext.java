package cn.org.faster.framework.builder.common.context;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.core.utils.Utils;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
@Data
public abstract class BuildContext {
    protected BuilderModel builderModel;
    protected List<BuildStrategy> strategyList = new ArrayList<>();


    public BuildContext(BuilderModel builderModel) {
        this.builderModel = builderModel;
    }

    /**
     * 处理流程
     *
     * @return
     */
    public byte[] process() throws IOException {
        //初始化生成策略
        initBuildStrategy(strategyList);
        //创建压缩文件
        File zipFile = File.createTempFile(builderModel.getProjectName(), ".zip");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            strategyList.forEach(item -> item.setZipOutputStream(zipOutputStream));
            for (BuildStrategy buildStrategy : strategyList) {
                buildStrategy.process();
            }
        }
        return Utils.inputStreamToByteArray(new FileInputStream(zipFile));
    }

    /**
     * 初始化策略集合，设置希望参与本次生成的策略。
     *
     * @param strategyList 策略集合
     */
    public abstract void initBuildStrategy(List<BuildStrategy> strategyList);
}
