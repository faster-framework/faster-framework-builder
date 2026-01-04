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
 * @since 2018/12/15
 */
public class MapperStrategy extends ApiStrategyAdapter {
    public MapperStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template mapperTemp = FreemarkerUtils.cfg.getTemplate("java/mapper.ftl");
        List<TableColumnModel> columnModelList = builderModel.getTableColumnList();
        for (TableColumnModel tableColumnModel : columnModelList) {
            //文件名称
            String zipFileName = srcPath + tableColumnModel.getBusinessEnName() + "/mapper/" + tableColumnModel.getBusinessEnNameUpFirst() + "Mapper.java";
            //包名
            String packageStr = "package " + basePackage + "." + tableColumnModel.getBusinessEnName() + ".mapper;\n";
            StringBuffer importStr = new StringBuffer();
            importStr.append("import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n")
                    .append("import ").append(basePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".entity.").append(tableColumnModel.getBusinessEnNameUpFirst()).append(";\n");
            Map<String, Object> map = Utils.beanToMap(tableColumnModel);
            map.put("package", packageStr);
            map.put("import", importStr);
            zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
            zipOutputStream.write(FreemarkerUtils.processIntoStream(mapperTemp, map));
            zipOutputStream.closeEntry();
        }
    }
}
