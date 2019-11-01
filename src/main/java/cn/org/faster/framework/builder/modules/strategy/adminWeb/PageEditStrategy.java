package cn.org.faster.framework.builder.modules.strategy.adminWeb;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.model.ColumnModel;
import cn.org.faster.framework.builder.common.model.TableColumnModel;
import cn.org.faster.framework.builder.common.strategy.adapter.WebStrategyAdapter;
import cn.org.faster.framework.builder.common.utils.BuilderUtils;
import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
import cn.org.faster.framework.core.utils.Utils;
import freemarker.template.Template;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/17
 */
public class PageEditStrategy extends WebStrategyAdapter {
    public PageEditStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template editTemp = FreemarkerUtils.cfg.getTemplate("/adminWeb/Edit.jsx.ftl");
        List<TableColumnModel> columnModelList = builderModel.getTableColumnList();
        for (TableColumnModel tableColumnModel : columnModelList) {
            String zipFileName = srcPath + tableColumnModel.getBusinessEnName() + "/" + tableColumnModel.getBusinessEnNameUpFirst() + "Edit.jsx";
            List<ColumnModel> columnList = tableColumnModel.getColumnList().stream()
                    .filter(item -> BuilderUtils.baseNotContainsProperty(item.getColumnNameHump()))
                    .collect(Collectors.toList());
            Map<String, Object> map = Utils.beanToMap(tableColumnModel);
            map.put("columnList", columnList);
            zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
            zipOutputStream.write(FreemarkerUtils.processIntoStream(editTemp, map));
            zipOutputStream.closeEntry();
        }
    }
}
