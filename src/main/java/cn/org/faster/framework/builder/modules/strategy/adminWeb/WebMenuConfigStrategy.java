package cn.org.faster.framework.builder.modules.strategy.adminWeb;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.adapter.WebStrategyAdapter;
import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
import freemarker.template.Template;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/17
 */
public class WebMenuConfigStrategy extends WebStrategyAdapter {
    public WebMenuConfigStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template menuConfigTemp = FreemarkerUtils.cfg.getTemplate("/adminWeb/menuConfig.js.ftl");
        String zipFileName = basePath + "menuConfig.js";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(menuConfigTemp, builderModel));
        zipOutputStream.closeEntry();
    }
}
