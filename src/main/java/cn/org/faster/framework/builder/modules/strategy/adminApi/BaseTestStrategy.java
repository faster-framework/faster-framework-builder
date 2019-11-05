package cn.org.faster.framework.builder.modules.strategy.adminApi;

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
public class BaseTestStrategy extends BuildStrategy {
    public BaseTestStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template baseTestTemp = FreemarkerUtils.cfg.getTemplate("adminApi/baseTest.ftl");
        String zipFileName = BuilderConstants.JAVA_TEST_PATH + builderModel.getBasePath() + "/test/BaseTest.java";
        //包名
        String packageStr = "package " + builderModel.getBasePackagePath() + ".test;\n";
        String importStr = "import org.junit.Before;\n" +
                "import org.apache.shiro.web.servlet.AbstractShiroFilter;\n" +
                "import "+builderModel.getBasePackagePath()+".Application;\n" +
                "import org.junit.runner.RunWith;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.boot.test.context.SpringBootTest;\n" +
                "import org.springframework.http.HttpHeaders;\n" +
                "import org.springframework.http.MediaType;\n" +
                "import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\n" +
                "import org.springframework.test.web.servlet.MockMvc;\n" +
                "import org.springframework.test.web.servlet.ResultActions;\n" +
                "import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;\n" +
                "import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;\n" +
                "import org.springframework.web.context.WebApplicationContext;\n" +
                "import java.nio.charset.StandardCharsets;\n" +
                "import java.util.function.Supplier;\n" +
                "import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;\n" +
                "import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;\n";
        ;
        Map<String, Object> map = Utils.beanToMap(builderModel);
        map.put("package", packageStr);
        map.put("import", importStr);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(baseTestTemp, map));
        zipOutputStream.closeEntry();
    }
}
