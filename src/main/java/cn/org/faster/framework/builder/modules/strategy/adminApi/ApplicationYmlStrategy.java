package cn.org.faster.framework.builder.modules.strategy.adminApi;

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
public class ApplicationYmlStrategy extends BuildStrategy {
    public ApplicationYmlStrategy(BuilderModel builderModel) {
        super(builderModel);

    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template ymlTemp = FreemarkerUtils.cfg.getTemplate("adminApi/application.yml.ftl");
        String zipFileName = BuilderConstants.JAVA_RESOURCES_PATH + "application.yml";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(ymlTemp, builderModel));
        zipOutputStream.closeEntry();
    }
}
