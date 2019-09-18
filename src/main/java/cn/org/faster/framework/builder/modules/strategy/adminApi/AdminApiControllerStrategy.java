package cn.org.faster.framework.builder.modules.strategy.adminApi;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.model.TableColumnModel;
import cn.org.faster.framework.builder.common.strategy.adapter.JavaStrategyAdapter;
import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
import cn.org.faster.framework.core.utils.Utils;
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
public class AdminApiControllerStrategy extends JavaStrategyAdapter {

    public AdminApiControllerStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template controllerTemp = FreemarkerUtils.cfg.getTemplate("adminApi/controller.ftl");
        List<TableColumnModel> columnModelList = builderModel.getTableColumnList();
        for (TableColumnModel tableColumnModel : columnModelList) {
            //文件名称
            String zipFileName = basePath + tableColumnModel.getBusinessEnName() + "/controller/" + tableColumnModel.getBusinessEnNameUpFirst() + "Controller.java";
            //包名
            String packageStr = "package " + basePackage + "." + tableColumnModel.getBusinessEnName() + ".controller;\n";
            StringBuffer importStr = new StringBuffer()
                    .append("import lombok.AllArgsConstructor;\n")
                    .append("import org.springframework.http.ResponseEntity;\n")
                    .append("import org.springframework.web.bind.annotation.*;\n")
                    .append("import org.springframework.beans.factory.annotation.Autowired;\n")
                    .append("import org.springframework.validation.annotation.Validated;\n")
                    .append("import org.springframework.beans.BeanUtils;\n")
                    .append("import org.apache.shiro.authz.annotation.RequiresPermissions;\n")
                    .append("import ").append(basePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".service.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("Service;\n")
                    .append("import ").append(basePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".model.request.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("AddRequest;\n")
                    .append("import ").append(basePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".model.request.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("QueryRequest;\n")
                    .append("import ").append(basePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".model.request.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("ListRequest;\n")
                    .append("import ").append(basePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".model.request.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("UpdateRequest;\n")
                    .append("import ").append(basePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".entity.").append(tableColumnModel.getBusinessEnNameUpFirst()).append(";\n");
            Map<String, Object> map = Utils.beanToMap(tableColumnModel);
            map.put("package", packageStr);
            map.put("import", importStr);
            zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
            zipOutputStream.write(FreemarkerUtils.processIntoStream(controllerTemp, map));
            zipOutputStream.closeEntry();
        }

    }
}
