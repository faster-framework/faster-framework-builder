package cn.org.faster.framework.builder.common.context;

import cn.org.faster.framework.builder.common.compression.EntryNormalizingZipOutputStream;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成流程骨架：由子类决定"用哪些策略、按什么顺序"，父类负责组装 ZIP。
 *
 * @author zhangbowen
 * @since 2018/12/14
 */
public abstract class BuildContext {

    protected final BuilderModel builderModel;
    protected final List<BuildStrategy> strategyList = new ArrayList<>();

    protected BuildContext(BuilderModel builderModel) {
        this.builderModel = builderModel;
    }

    /**
     * 执行生成。
     *
     * @return ZIP 字节内容
     * @throws IOException 写入失败
     */
    public byte[] process() throws IOException {
        initBuildStrategy(strategyList);
        var buffer = new ByteArrayOutputStream(128 * 1024);
        try (var zipOutputStream = new EntryNormalizingZipOutputStream(buffer)) {
            for (BuildStrategy buildStrategy : strategyList) {
                buildStrategy.process(zipOutputStream);
            }
        }
        return buffer.toByteArray();
    }

    /**
     * 初始化策略集合，设置希望参与本次生成的策略。
     *
     * @param strategyList 策略集合
     */
    protected abstract void initBuildStrategy(List<BuildStrategy> strategyList);
}
