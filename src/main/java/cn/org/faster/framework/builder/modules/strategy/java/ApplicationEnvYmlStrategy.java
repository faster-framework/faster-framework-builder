package cn.org.faster.framework.builder.modules.strategy.java;

import cn.org.faster.framework.builder.common.constants.BuilderConstants;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
import freemarker.template.Template;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/15
 */
public class ApplicationEnvYmlStrategy extends BuildStrategy {
    public ApplicationEnvYmlStrategy(BuilderModel builderModel) {
        super(builderModel);

    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template localYmlTemp = FreemarkerUtils.cfg.getTemplate("java/application-local.yml.ftl");
        String zipFileName = BuilderConstants.JAVA_RESOURCES_PATH + "application-local.yml";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(localYmlTemp, builderModel));
        zipOutputStream.closeEntry();
        Template devYmlTemp = FreemarkerUtils.cfg.getTemplate("java/application-dev.yml.ftl");
        zipFileName = BuilderConstants.JAVA_RESOURCES_PATH + "application-dev.yml";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(devYmlTemp, builderModel));
        zipOutputStream.closeEntry();
        Template testYmlTemp = FreemarkerUtils.cfg.getTemplate("java/application-test.yml.ftl");
        zipFileName = BuilderConstants.JAVA_RESOURCES_PATH + "application-test.yml";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(testYmlTemp, builderModel));
        zipOutputStream.closeEntry();
        Template prodYmlTemp = FreemarkerUtils.cfg.getTemplate("java/application-prod.yml.ftl");
        zipFileName = BuilderConstants.JAVA_RESOURCES_PATH + "application-prod.yml";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(prodYmlTemp, builderModel));
        zipOutputStream.closeEntry();
    }
}
