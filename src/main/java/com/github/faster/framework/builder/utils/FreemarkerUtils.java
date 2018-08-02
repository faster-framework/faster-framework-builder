package com.github.faster.framework.builder.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FreemarkerUtils {
    public static Configuration cfg;

    static {
        try {
            FreeMarkerConfigurationFactory configurationFactory = new FreeMarkerConfigurationFactory();
            configurationFactory.setTemplateLoaderPath("classpath:/template");
            cfg = configurationFactory.createConfiguration();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
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
