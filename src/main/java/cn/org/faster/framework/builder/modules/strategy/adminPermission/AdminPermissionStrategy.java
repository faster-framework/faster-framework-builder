package cn.org.faster.framework.builder.modules.strategy.adminPermission;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.strategy.BuildStrategy;
import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
import freemarker.template.Template;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/15
 */
public class AdminPermissionStrategy extends BuildStrategy {
    public AdminPermissionStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template permissionTemp = FreemarkerUtils.cfg.getTemplate("/adminPermission/permission.sql.ftl");
        String zipFileName = builderModel.getProjectName() + ".sql";
        List<PermissionModel> permissionModelList = builderModel.getTableColumnList().stream().flatMap(item -> {
            List<PermissionModel> businessPermissionModelList = new ArrayList<>();
            PermissionModel parent = new PermissionModel();
            parent.setName(item.getBusinessCnName() + "管理");
            parent.setCode(item.getBusinessEnName() + ":manage");
            parent.setParentId(0L);
            parent.setParentIds("[0]");
            parent.preInsert();
            PermissionModel add = new PermissionModel();
            add.setName(item.getBusinessCnName() + "添加");
            add.setCode(item.getBusinessEnName() + ":add");
            add.setParentId(parent.getId());
            add.setParentIds(parent.getParentIds() + ",[" + parent.getId() + "]");
            add.preInsert();
            PermissionModel update = new PermissionModel();
            BeanUtils.copyProperties(add, update);
            update.setName(item.getBusinessCnName() + "编辑");
            update.setCode(item.getBusinessEnName() + ":modify");
            update.preInsert();
            PermissionModel list = new PermissionModel();
            BeanUtils.copyProperties(add, list);
            list.setName(item.getBusinessCnName() + "列表");
            list.setCode(item.getBusinessEnName() + ":list");
            list.preInsert();
            PermissionModel delete = new PermissionModel();
            BeanUtils.copyProperties(add, delete);
            delete.setName(item.getBusinessCnName() + "删除");
            delete.setCode(item.getBusinessEnName() + ":delete");
            delete.preInsert();
            PermissionModel info = new PermissionModel();
            BeanUtils.copyProperties(add, info);
            info.setName(item.getBusinessCnName() + "详情");
            info.setCode(item.getBusinessEnName() + ":info");
            info.preInsert();
            businessPermissionModelList.add(parent);
            businessPermissionModelList.add(add);
            businessPermissionModelList.add(update);
            businessPermissionModelList.add(list);
            businessPermissionModelList.add(delete);
            businessPermissionModelList.add(info);
            return businessPermissionModelList.stream();
        }).collect(Collectors.toList());
        Map<String, Object> param = new HashMap<>();
        param.put("permissionList", permissionModelList);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(permissionTemp, param));
        zipOutputStream.closeEntry();
    }
}
