package cn.org.faster.framework.builder.common.utils;

import cn.org.faster.framework.builder.common.exception.BuilderException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * FreeMarker 渲染工具。
 *
 * <p>与旧版的差异：不再依赖 Spring 的 {@code FreeMarkerConfigurationFactory}，
 * 且模板异常一律向上抛出（旧版 {@code printStackTrace} 会把失败伪装成成功）。</p>
 *
 * @author zhangbowen
 */
public final class FreemarkerUtils {

    /**
     * 全局模板配置，模板根路径为 {@code classpath:/template}。
     */
    public static final Configuration cfg;

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        cfg.setTemplateLoader(new NormalizingClassTemplateLoader());
    }

    private FreemarkerUtils() {
    }

    /**
     * 渲染模板。
     *
     * @param template  模板
     * @param dataModel 数据模型
     * @return 渲染结果
     */
    public static byte[] processIntoStream(Template template, Object dataModel) {
        var byteArrayOutputStream = new ByteArrayOutputStream(16 * 1024);
        try (Writer out = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)) {
            template.process(dataModel, out);
        } catch (IOException | TemplateException ex) {
            throw new BuilderException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "模板 " + template.getName() + " 渲染失败：" + ex.getMessage(), ex);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 兼容策略中带前导 {@code /} 的模板名（如 {@code /db/base.sql.ftl}）。
     */
    private static final class NormalizingClassTemplateLoader implements TemplateLoader {

        private final ClassTemplateLoader delegate =
                new ClassTemplateLoader(FreemarkerUtils.class, "/template/");

        @Override
        public Object findTemplateSource(String name) throws IOException {
            return delegate.findTemplateSource(normalize(name));
        }

        @Override
        public long getLastModified(Object templateSource) {
            return delegate.getLastModified(templateSource);
        }

        @Override
        public Reader getReader(Object templateSource, String encoding) throws IOException {
            return delegate.getReader(templateSource, encoding);
        }

        @Override
        public void closeTemplateSource(Object templateSource) throws IOException {
            delegate.closeTemplateSource(templateSource);
        }

        private static String normalize(String name) {
            return name != null && name.startsWith("/") ? name.substring(1) : name;
        }
    }
}
