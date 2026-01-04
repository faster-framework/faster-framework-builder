package cn.org.faster.framework.builder.modules.strategy.java;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
import freemarker.template.Template;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public class GitIgnoreStrategy extends BuildStrategy {

    public GitIgnoreStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template gitIgnoreTemp = FreemarkerUtils.cfg.getTemplate("java/gitIgnore.ftl");
        String zipFileName = ".gitignore";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(gitIgnoreTemp, builderModel));
        zipOutputStream.closeEntry();
    }
}
