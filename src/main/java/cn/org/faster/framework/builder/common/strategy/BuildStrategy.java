package cn.org.faster.framework.builder.common.strategy;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import lombok.Data;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
@Data
public abstract class BuildStrategy {
    protected BuilderModel builderModel;
    protected String srcPath;

    public BuildStrategy(BuilderModel builderModel) {
        this.builderModel = builderModel;
    }

    /**
     * 处理流程
     *
     * @return 字节数组
     */
    public abstract void process(ZipOutputStream zipOutputStream) throws IOException;

}
