package cn.org.faster.framework.builder.modules.strategy.adminWeb;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.adapter.WebStrategyAdapter;
import cn.org.faster.framework.core.utils.Utils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/17
 */
public class WebProjectFrameworkStrategy extends WebStrategyAdapter {
    private List<String> skipFileNames = Arrays.asList("/config/router-modules.js", "/config/defaultSettings.js");

    public WebProjectFrameworkStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.exchange(
                builderModel.getDependencyUrl(),
                HttpMethod.GET,
                new HttpEntity<byte[]>(new HttpHeaders()),
                byte[].class);
        File downloadTempFile = File.createTempFile(System.currentTimeMillis() + "", ".zip");
        try (FileOutputStream outputStream = new FileOutputStream(downloadTempFile)) {
            outputStream.write(Objects.requireNonNull(response.getBody()));
        }
        ZipFile zipFile = new ZipFile(downloadTempFile);
        zipFile.stream().filter(ze -> {
            boolean isNotDirectory = !ze.isDirectory();
            String fileName = ze.getName();
            int index = fileName.indexOf("/");
            fileName = fileName.substring(index);
            boolean isNotNeedSkip = !skipFileNames.contains(fileName);
            return isNotDirectory && isNotNeedSkip;
        }).forEach(ze -> {
            String fileName = ze.getName();
            int index = fileName.indexOf("/");
            fileName = fileName.substring(index);
            try {
                zipOutputStream.putNextEntry(new ZipEntry(fileName));
                zipOutputStream.write(StreamUtils.copyToByteArray(zipFile.getInputStream(ze)));
                zipOutputStream.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
