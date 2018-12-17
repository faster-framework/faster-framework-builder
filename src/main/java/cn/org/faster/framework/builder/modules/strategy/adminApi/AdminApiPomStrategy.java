package cn.org.faster.framework.builder.modules.strategy.adminApi;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.model.DependencyModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
import cn.org.faster.framework.core.utils.Utils;
import freemarker.template.Template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/15
 */
public class AdminApiPomStrategy extends BuildStrategy {
    public AdminApiPomStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        builderModel.setDependencyVersion(builderModel.getDependencyVersion().replace("v", ""));
        Template pomTemp = FreemarkerUtils.cfg.getTemplate("java/pom.xml.ftl");
        String zipFileName = "pom.xml";
        Map<String, Object> map = Utils.beanToMap(builderModel);
        List<DependencyModel> dependencyModelList = new ArrayList<>();
        dependencyModelList.add(new DependencyModel("cn.org.faster", "spring-boot-starter-web"));
        dependencyModelList.add(new DependencyModel("cn.org.faster", "spring-boot-starter-mybatis"));
        dependencyModelList.add(new DependencyModel("cn.org.faster", "spring-boot-starter-admin"));
        map.put("dependencies", dependencyModelList);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(pomTemp, map));
        zipOutputStream.closeEntry();
    }
}
