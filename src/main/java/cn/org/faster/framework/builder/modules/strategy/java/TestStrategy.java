package cn.org.faster.framework.builder.modules.strategy.java;

import cn.org.faster.framework.builder.common.constants.BuilderConstants;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.model.TableColumnModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
import cn.org.faster.framework.core.utils.Utils;
import cn.org.faster.framework.web.spring.boot.autoconfigure.ProjectProperties;
import cn.org.faster.framework.web.web.service.JwtService;
import freemarker.template.Template;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public class TestStrategy extends BuildStrategy {

    public TestStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template testTemp = FreemarkerUtils.cfg.getTemplate("java/test.ftl");
        List<TableColumnModel> columnModelList = builderModel.getTableColumnList();
        basePath = BuilderConstants.JAVA_TEST_PATH + builderModel.getBasePath() + "/test/modules/v1/";
        String basePackage = builderModel.getBasePackagePath() + ".test.modules.v1";
        for (TableColumnModel tableColumnModel : columnModelList) {
            //文件名称
            String zipFileName = basePath + tableColumnModel.getBusinessEnName() + "/" + tableColumnModel.getBusinessEnNameUpFirst() + "Test.java";
            //包名
            String packageStr = "package " + basePackage + "." + tableColumnModel.getBusinessEnName() + ";\n";
            StringBuffer importStr = new StringBuffer()
                    .append("import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;\n")
                    .append("import org.junit.Test;\n")
                    .append("import ").append(builderModel.getBasePackagePath()).append(".test.BaseTest;\n")
                    .append("import com.alibaba.fastjson.JSON;\n")
                    .append("import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;\n")
                    .append("import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;\n")
                    .append("import ").append(builderModel.getBasePackagePath()).append(".modules.v1").append(".").append(tableColumnModel.getBusinessEnName()).append(".model.request.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("AddRequest;\n")
                    .append("import ").append(builderModel.getBasePackagePath()).append(".modules.v1").append(".").append(tableColumnModel.getBusinessEnName()).append(".model.request.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("UpdateRequest;\n");
            Map<String, Object> map = Utils.beanToMap(tableColumnModel);
            map.put("package", packageStr);
            map.put("import", importStr);
            JwtService jwtService = new JwtService();
            jwtService.setBase64Security(new ProjectProperties().getBase64Secret());
            map.put("headerToken", jwtService.createToken(0L, 0));
            zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
            zipOutputStream.write(FreemarkerUtils.processIntoStream(testTemp, map));
            zipOutputStream.closeEntry();
        }

    }
}
