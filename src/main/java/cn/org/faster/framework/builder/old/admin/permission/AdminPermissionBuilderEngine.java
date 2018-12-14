//package cn.org.faster.framework.builder.old.admin.permission;
//
//import cn.org.faster.framework.builder.old.engine.BuilderEngine;
//import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
//import cn.org.faster.framework.core.utils.Utils;
//import freemarker.template.Template;
//import org.springframework.beans.BeanUtils;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//public class AdminPermissionBuilderEngine extends BuilderEngine {
//    private static final String ADMIN_PERMISSION_TEMPLATE_DIR = "permission";
//    private Template permissionTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_PERMISSION_TEMPLATE_DIR + "/permission.sql.ftl");
//
//    public AdminPermissionBuilderEngine() throws IOException {
//    }
//
//
//    @Override
//    public byte[] start() throws IOException {
//        //创建压缩文件
//        File zipFile = File.createTempFile(builderParam.getProjectName(), ".zip");
//        //创建模板
//        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
//            processPermission(zipOutputStream);
//        }
//        return Utils.inputStreamToByteArray(new FileInputStream(zipFile));
//    }
//
//    /**
//     * 生成权限sql
//     *
//     * @param zipOutputStream 压缩流
//     * @throws IOException io异常
//     */
//    private void processPermission(ZipOutputStream zipOutputStream) throws IOException {
//        String zipFileName = builderParam.getProjectName() + ".sql";
//        List<PermissionModel> permissionModelList = builderParam.getTableColumnList().stream().flatMap(item -> {
//            List<PermissionModel> businessPermissionModelList = new ArrayList<>();
//            PermissionModel parent = new PermissionModel();
//            parent.setName(item.getBusinessCnName() + "管理");
//            parent.setCode(item.getBusinessEnName() + ":manage");
//            parent.setParentId(0L);
//            parent.setParentIds("[0]");
//            parent.preInsert();
//            PermissionModel add = new PermissionModel();
//            add.setName(item.getBusinessCnName() + "添加");
//            add.setCode(item.getBusinessEnName() + ":add");
//            add.setParentId(parent.getId());
//            add.setParentIds(parent.getParentIds() + ",[" + parent.getId() + "]");
//            add.preInsert();
//            PermissionModel update = new PermissionModel();
//            BeanUtils.copyProperties(add, update);
//            update.setName(item.getBusinessCnName() + "编辑");
//            update.setCode(item.getBusinessEnName() + ":modify");
//            update.preInsert();
//            PermissionModel list = new PermissionModel();
//            BeanUtils.copyProperties(add, list);
//            list.setName(item.getBusinessCnName() + "列表");
//            list.setCode(item.getBusinessEnName() + ":list");
//            list.preInsert();
//            PermissionModel delete = new PermissionModel();
//            BeanUtils.copyProperties(add, delete);
//            delete.setName(item.getBusinessCnName() + "删除");
//            delete.setCode(item.getBusinessEnName() + ":delete");
//            delete.preInsert();
//            PermissionModel info = new PermissionModel();
//            BeanUtils.copyProperties(add, info);
//            info.setName(item.getBusinessCnName() + "详情");
//            info.setCode(item.getBusinessEnName() + ":info");
//            info.preInsert();
//            businessPermissionModelList.add(parent);
//            businessPermissionModelList.add(add);
//            businessPermissionModelList.add(update);
//            businessPermissionModelList.add(list);
//            businessPermissionModelList.add(delete);
//            businessPermissionModelList.add(info);
//            return businessPermissionModelList.stream();
//        }).collect(Collectors.toList());
//        Map<String, Object> param = new HashMap<>();
//        param.put("permissionList", permissionModelList);
//        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
//        zipOutputStream.write(FreemarkerUtils.processIntoStream(permissionTemp, param));
//        zipOutputStream.closeEntry();
//    }
//}
