package cn.org.faster.framework.builder.modules.strategy.adminApi;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.net.FrameworkArchiveCopier;
import cn.org.faster.framework.builder.common.net.SkeletonDownloader;
import cn.org.faster.framework.builder.common.strategy.adapter.AdminApiStrategyAdapter;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import java.util.zip.ZipOutputStream;

/**
 * 下载官方 admin-api 骨架并合并到生成结果。
 *
 * @author zhangbowen
 * @since 2018/12/17
 */
public class ProjectFrameworkStrategy extends AdminApiStrategyAdapter {

    private static final Set<String> SKIP_ENTRY_NAMES = Set.of("/.gitignore", "/pom.xml");

    public ProjectFrameworkStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        byte[] skeleton = SkeletonDownloader.download(
                builderModel.getDependencyUrl(),
                Duration.ofMillis(builderModel.getSkeletonTimeoutMillis()),
                builderModel.getSkeletonMaxBytes());
        FrameworkArchiveCopier.copyInto(skeleton, SKIP_ENTRY_NAMES, zipOutputStream);
    }
}
