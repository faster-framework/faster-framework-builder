package com.github.faster.framework.builder.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class FreemarkerUtils {
    public static final Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);

    static {
        try {
            cfg.setDirectoryForTemplateLoading(new ClassPathResource("template").getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    /**
     * @param template  模板
     * @param dataModel 传入数据
     * @return 返回字节数组
     */
    public static byte[] processIntoStream(Template template, Object dataModel) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (Writer out = new OutputStreamWriter(byteArrayOutputStream)) {
            template.process(dataModel, out);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

}
