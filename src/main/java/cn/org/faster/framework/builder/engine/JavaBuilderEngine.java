package cn.org.faster.framework.builder.engine;

import cn.org.faster.framework.builder.engine.BuilderEngine;
import cn.org.faster.framework.builder.model.TableColumnModel;
import cn.org.faster.framework.builder.model.request.BuilderRequest;
import cn.org.faster.framework.builder.utils.FreemarkerUtils;
import cn.org.faster.framework.core.utils.Utils;
import com.alibaba.druid.filter.config.ConfigTools;
import freemarker.template.Template;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public abstract class JavaBuilderEngine extends BuilderEngine {
    private static final String JAVA_TEMPLATE_DIR = "java";

    protected final String JAVA_PATH = "/src/main/java/";
    protected final String TEST_JAVA_PATH = "/src/test/java/";
    protected final String RESOURCES_PATH = "/src/main/resources/";
    private Template settingsGradleTemp = FreemarkerUtils.cfg.getTemplate(JAVA_TEMPLATE_DIR + "/settings.gradle.ftl");
    private Template mapperTemp = FreemarkerUtils.cfg.getTemplate(JAVA_TEMPLATE_DIR + "/mapper.ftl");
    private Template gitIgnoreTemp = FreemarkerUtils.cfg.getTemplate(JAVA_TEMPLATE_DIR + "/gitIgnore.ftl");
    private Template applicationTemp = FreemarkerUtils.cfg.getTemplate(JAVA_TEMPLATE_DIR + "/application.ftl");

    protected JavaBuilderEngine() throws IOException {
    }

    /**
     *
     * @return 模块路径
     */
    protected abstract String getBaseModulePath();

    /**
     *
     * @return 模块包
     */
    protected abstract String getBaseModulePackage();

    @Override
    protected void init(BuilderRequest builderRequest, List<TableColumnModel> tableColumnList) {
        super.init(builderRequest, tableColumnList);
        super.builderParam.setDependencyVersion(builderParam.getDependencyVersion().replace("v",""));
        try {
            String[] arr = ConfigTools.genKeyPair(512);
            super.builderParam.setDbEncryptPwd(ConfigTools.encrypt(arr[0], builderParam.getDbPwd()));
            super.builderParam.setDbPublicKey(arr[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理mapper
     *
     * @param tableColumnModel 模型
     * @param zipOutputStream  压缩文件
     * @throws IOException IO异常
     */
    protected void processMapper(TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        //文件名称
        String zipFileName = getBaseModulePath() + tableColumnModel.getBusinessEnName() + "/mapper/" + tableColumnModel.getBusinessEnNameUpFirst() + "Mapper.java";
        //包名
        String packageStr = "package " + getBaseModulePackage() + "." + tableColumnModel.getBusinessEnName() + ".mapper;\n";
        StringBuffer importStr = new StringBuffer();
        importStr.append("import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n")
                .append("import ").append(getBaseModulePackage()).append(".").append(tableColumnModel.getBusinessEnName()).append(".entity.").append(tableColumnModel.getBusinessEnNameUpFirst()).append(";\n");
        Map<String, Object> map = Utils.beanToMap(tableColumnModel);
        map.put("package", packageStr);
        map.put("import", importStr);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(mapperTemp, map));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成settings.gradle
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    protected void processSettingsGradle(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = "settings.gradle";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(settingsGradleTemp, builderParam));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成gitIgnore
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    protected void processGitIgnore(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = ".gitignore";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(gitIgnoreTemp, builderParam));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成application
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    protected void processApplication(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = JAVA_PATH + builderParam.getBasePath() + "/Application.java";
        //包名
        String packageStr = "package " + builderParam.getBasePackagePath() + ";\n";
        StringBuffer importStr = new StringBuffer();
        importStr
                .append("import org.springframework.boot.SpringApplication;\n")
                .append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n")
        ;
        Map<String, Object> map = Utils.beanToMap(builderParam);
        map.put("package", packageStr);
        map.put("import", importStr);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(applicationTemp, map));
        zipOutputStream.closeEntry();
    }

}
