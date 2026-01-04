package cn.org.faster.framework.builder.modules.strategy.java;

import cn.org.faster.framework.builder.common.constants.BuilderConstants;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
import cn.org.faster.framework.core.utils.Utils;
import freemarker.template.Template;

import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/15
 */
public class SpringBootApplicationStrategy extends BuildStrategy {
    public SpringBootApplicationStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template applicationTemp = FreemarkerUtils.cfg.getTemplate("java/springboot-application.ftl");
        String zipFileName = BuilderConstants.JAVA_PATH + builderModel.getBasePath() + "/Application.java";
        //包名
        String packageStr = "package " + builderModel.getBasePackagePath() + ";\n";
        StringBuffer importStr = new StringBuffer();
        importStr
                .append("import org.springframework.boot.SpringApplication;\n")
                .append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n")
        ;
        Map<String, Object> map = Utils.beanToMap(builderModel);
        map.put("package", packageStr);
        map.put("import", importStr);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(applicationTemp, map));
        zipOutputStream.closeEntry();
    }
}
