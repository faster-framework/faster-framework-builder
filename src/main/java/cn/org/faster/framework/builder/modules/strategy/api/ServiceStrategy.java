package cn.org.faster.framework.builder.modules.strategy.api;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.model.TableColumnModel;
import cn.org.faster.framework.builder.common.strategy.adapter.ApiStrategyAdapter;
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
public class ServiceStrategy extends ApiStrategyAdapter {

    public ServiceStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template serviceTemp = FreemarkerUtils.cfg.getTemplate("java/service.ftl");
        List<TableColumnModel> columnModelList = builderModel.getTableColumnList();
        for (TableColumnModel tableColumnModel : columnModelList) {
            //文件名称
            String zipFileName = srcPath + tableColumnModel.getBusinessEnName() + "/service/" + tableColumnModel.getBusinessEnNameUpFirst() + "Service.java";
            //包名
            String packageStr = "package " + basePackage + "." + tableColumnModel.getBusinessEnName() + ".service;\n";
            StringBuffer importStr = new StringBuffer()
                    .append("import com.baomidou.mybatisplus.core.metadata.IPage;\n")
                    .append("import org.springframework.util.StringUtils;\n")
                    .append("import org.springframework.http.HttpStatus;\n")
                    .append("import org.springframework.http.ResponseEntity;\n")
                    .append("import org.springframework.stereotype.Service;\n")
                    .append("import org.springframework.transaction.annotation.Transactional;\n")
                    .append("import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\n")
                    .append("import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\n")
                    .append("import ").append(basePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".mapper.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("Mapper;\n")
                    .append("import ").append(basePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".entity.").append(tableColumnModel.getBusinessEnNameUpFirst()).append(";\n");
            Map<String, Object> map = Utils.beanToMap(tableColumnModel);
            map.put("package", packageStr);
            map.put("import", importStr);
            map.put("columnList", tableColumnModel.getColumnList());
            zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
            zipOutputStream.write(FreemarkerUtils.processIntoStream(serviceTemp, map));
            zipOutputStream.closeEntry();
        }

    }
}
