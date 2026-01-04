package cn.org.faster.framework.builder.modules.strategy.db;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
import freemarker.template.Template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/15
 */
public class BaseDbStrategy extends BuildStrategy {
    public BaseDbStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template baseDbTemp = FreemarkerUtils.cfg.getTemplate("/db/base.sql.ftl");
        String zipFileName = "base.sql";
        Map<String, Object> param = new HashMap<>();
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(baseDbTemp, param));
        zipOutputStream.closeEntry();
    }
}
