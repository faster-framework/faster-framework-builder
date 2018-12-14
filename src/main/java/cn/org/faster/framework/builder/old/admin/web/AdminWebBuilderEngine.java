//package cn.org.faster.framework.builder.old.admin.web;
//
//import cn.org.faster.framework.builder.old.engine.BuilderEngine;
//import cn.org.faster.framework.builder.common.model.ColumnModel;
//import cn.org.faster.framework.builder.common.model.TableColumnModel;
//import cn.org.faster.framework.builder.common.utils.BuilderUtils;
//import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
//import cn.org.faster.framework.core.utils.Utils;
//import freemarker.template.Template;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;
//import java.util.zip.ZipOutputStream;
//
//public class AdminWebBuilderEngine extends BuilderEngine {
//    private String baseModulePath;
//    private static final String ADMIN_WEB_TEMPLATE_DIR = "web";
//    private Template packageJsonTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/package.json.ftl");
//    private Template AddTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/Add.jsx.ftl");
//    private Template EditTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/Edit.jsx.ftl");
//    private Template ListTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/List.jsx.ftl");
//    private Template IndexTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/index.js.ftl");
//    private Template MenuConfigTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/menuConfig.js.ftl");
//    private Template RouterConfigTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/routerConfig.js.ftl");
//    private Template WebpackRcDevTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/.webpackrc.dev.js.ftl");
//    private Template WebpackRcProdTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/.webpackrc.prod.js.ftl");
//    private List<String> skipFileNames = Arrays.asList("/src/modules/menuConfig.js", "/package.json", "/src/modules/routerConfig.js", "/config/.webpackrc.dev.js", "/config/.webpackrc.prod.js");
//
//    public AdminWebBuilderEngine() throws IOException {
//    }
//
//
//    @Override
//    public byte[] start() throws IOException {
//        baseModulePath = "/src/modules/";
//        //创建压缩文件
//        File zipFile = File.createTempFile(builderParam.getProjectName(), ".zip");
//        //创建模板
//        List<TableColumnModel> columnModelList = builderParam.getTableColumnList();
//        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
//            processProject(zipOutputStream);
//            for (TableColumnModel tableColumnModel : columnModelList) {
//                this.processIndex(tableColumnModel, zipOutputStream);
//                this.processAdd(tableColumnModel, zipOutputStream);
//                this.processEdit(tableColumnModel, zipOutputStream);
//                this.processList(tableColumnModel, zipOutputStream);
//            }
//        }
//        return Utils.inputStreamToByteArray(new FileInputStream(zipFile));
//    }
//
//    /**
//     * 生成项目主框架
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processProject(ZipOutputStream zipOutputStream) throws IOException {
//        processProjectFramework(zipOutputStream);
//        processPackageJson(zipOutputStream);
//        processMenuConfig(zipOutputStream);
//        processRouterConfig(zipOutputStream);
//        processWebpackRcDev(zipOutputStream);
//        processWebpackRcProd(zipOutputStream);
//    }
//
//
//    /**
//     * 生成项目主框架
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processProjectFramework(ZipOutputStream zipOutputStream) throws IOException {
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<byte[]> response = restTemplate.exchange(
//                builderParam.getDependencyUrl(),
//                HttpMethod.GET,
//                new HttpEntity<byte[]>(new HttpHeaders()),
//                byte[].class);
//        File downloadTempFile = File.createTempFile(System.currentTimeMillis() + "", ".zip");
//        try (FileOutputStream outputStream = new FileOutputStream(downloadTempFile)) {
//            outputStream.write(response.getBody());
//        }
//        ZipFile zipFile = new ZipFile(downloadTempFile);
//        zipFile.stream().filter(ze -> {
//            boolean isNotDirectory = !ze.isDirectory();
//            String fileName = ze.getName();
//            int index = fileName.indexOf("/");
//            fileName = fileName.substring(index);
//            boolean isNotNeedSkip = !skipFileNames.contains(fileName);
//            return isNotDirectory && isNotNeedSkip;
//        }).forEach(ze -> {
//            String fileName = ze.getName();
//            int index = fileName.indexOf("/");
//            fileName = fileName.substring(index);
//            try {
//                zipOutputStream.putNextEntry(new ZipEntry(fileName));
//                zipOutputStream.write(Utils.inputStreamToByteArray(zipFile.getInputStream(ze)));
//                zipOutputStream.closeEntry();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    /**
//     * 生成packageJson文件
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processPackageJson(ZipOutputStream zipOutputStream) throws IOException {
//        String zipFileName = "package.json";
//        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
//        zipOutputStream.write(FreemarkerUtils.processIntoStream(packageJsonTemp, builderParam));
//        zipOutputStream.closeEntry();
//    }
//
//    /**
//     * 生成MenuConfig文件
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processMenuConfig(ZipOutputStream zipOutputStream) throws IOException {
//        String zipFileName = baseModulePath + "menuConfig.js";
//        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
//        zipOutputStream.write(FreemarkerUtils.processIntoStream(MenuConfigTemp, builderParam));
//        zipOutputStream.closeEntry();
//    }
//
//    /**
//     * 生成RouterConfig文件
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processRouterConfig(ZipOutputStream zipOutputStream) throws IOException {
//        String zipFileName = baseModulePath + "routerConfig.js";
//        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
//        zipOutputStream.write(FreemarkerUtils.processIntoStream(RouterConfigTemp, builderParam));
//        zipOutputStream.closeEntry();
//    }
//
//    /**
//     * 生成WebpackRcDev文件
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processWebpackRcDev(ZipOutputStream zipOutputStream) throws IOException {
//        String zipFileName = "/config/.webpackrc.dev.js";
//        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
//        zipOutputStream.write(FreemarkerUtils.processIntoStream(WebpackRcDevTemp, builderParam));
//        zipOutputStream.closeEntry();
//    }
//
//    /**
//     * 生成WebpackRcProd文件
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processWebpackRcProd(ZipOutputStream zipOutputStream) throws IOException {
//        String zipFileName = "/config/.webpackrc.prod.js";
//        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
//        zipOutputStream.write(FreemarkerUtils.processIntoStream(WebpackRcProdTemp, builderParam));
//        zipOutputStream.closeEntry();
//    }
//
//    /**
//     * 生成index.js
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processIndex(TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
//        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/index.js";
//        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
//        zipOutputStream.write(FreemarkerUtils.processIntoStream(IndexTemp, tableColumnModel));
//        zipOutputStream.closeEntry();
//    }
//
//    /**
//     * 生成add
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processAdd(TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
//        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/" + tableColumnModel.getBusinessEnNameUpFirst() + "Add.jsx";
//        List<ColumnModel> columnList = tableColumnModel.getColumnList().stream()
//                .filter(item -> BuilderUtils.baseNotContainsProperty(item.getColumnNameHump()))
//                .collect(Collectors.toList());
//        Map<String, Object> map = Utils.beanToMap(tableColumnModel);
//        map.put("columnList", columnList);
//        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
//        zipOutputStream.write(FreemarkerUtils.processIntoStream(AddTemp, map));
//        zipOutputStream.closeEntry();
//    }
//
//    /**
//     * 生成edit
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processEdit(TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
//        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/" + tableColumnModel.getBusinessEnNameUpFirst() + "Edit.jsx";
//        List<ColumnModel> columnList = tableColumnModel.getColumnList().stream()
//                .filter(item -> BuilderUtils.baseNotContainsProperty(item.getColumnNameHump()))
//                .collect(Collectors.toList());
//        Map<String, Object> map = Utils.beanToMap(tableColumnModel);
//        map.put("columnList", columnList);
//        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
//        zipOutputStream.write(FreemarkerUtils.processIntoStream(EditTemp, map));
//        zipOutputStream.closeEntry();
//    }
//
//    /**
//     * 生成List
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processList(TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
//        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/" + tableColumnModel.getBusinessEnNameUpFirst() + "List.jsx";
//        List<ColumnModel> columnList = tableColumnModel.getColumnList().stream()
//                .filter(item -> BuilderUtils.baseNotContainsProperty(item.getColumnNameHump()))
//                .collect(Collectors.toList());
//        Map<String, Object> map = Utils.beanToMap(tableColumnModel);
//        map.put("columnList", columnList);
//        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
//        zipOutputStream.write(FreemarkerUtils.processIntoStream(ListTemp, map));
//        zipOutputStream.closeEntry();
//    }
//}
