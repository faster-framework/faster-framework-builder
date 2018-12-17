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
public class WebPackageJsonStrategy extends WebStrategyAdapter {
    public WebPackageJsonStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template packageJsonTemp = FreemarkerUtils.cfg.getTemplate("/adminWeb/package.json.ftl");
        String zipFileName = "package.json";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(packageJsonTemp, builderModel));
        zipOutputStream.closeEntry();
    }
}
