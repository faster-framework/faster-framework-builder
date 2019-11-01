package cn.org.faster.framework.builder.modules.strategy.java;

import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.model.ColumnModel;
import cn.org.faster.framework.builder.common.model.TableColumnModel;
import cn.org.faster.framework.builder.common.strategy.adapter.JavaStrategyAdapter;
import cn.org.faster.framework.builder.common.utils.BuilderUtils;
import cn.org.faster.framework.builder.common.utils.FreemarkerUtils;
import cn.org.faster.framework.core.utils.Utils;
import freemarker.template.Template;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangbowen
 * @since 2018/12/14
 */
public class RequestStrategy extends JavaStrategyAdapter {

    public RequestStrategy(BuilderModel builderModel) {
        super(builderModel);
    }

    @Override
    public void process(ZipOutputStream zipOutputStream) throws IOException {
        Template requestTemp = FreemarkerUtils.cfg.getTemplate("java/request-model.ftl");
        List<TableColumnModel> columnModelList = builderModel.getTableColumnList();
        List<String> requestTypes = new ArrayList<>();
        requestTypes.add("AddRequest");
        requestTypes.add("UpdateRequest");
        requestTypes.add("ListRequest");
        requestTypes.add("QueryRequest");
        for (String requestType : requestTypes) {
            for (TableColumnModel tableColumnModel : columnModelList) {
                //文件名称
                String zipFileName = srcPath + tableColumnModel.getBusinessEnName() + "/model/request/" + tableColumnModel.getBusinessEnNameUpFirst() + requestType + ".java";
                //包名
                String packageStr = "package " + basePackage + "." + tableColumnModel.getBusinessEnName() + ".model.request;\n";
                StringBuffer importStr = new StringBuffer();
                importStr.append("import lombok.Data;\n")
                        .append("import cn.org.faster.framework.mybatis.entity.BaseEntity;\n")
                        .append("import lombok.EqualsAndHashCode;\n")
                ;
                List<ColumnModel> columnList = tableColumnModel.getColumnList().stream()
                        .filter(item -> BuilderUtils.baseNotContainsProperty(item.getColumnNameHump()))
                        .peek(item -> {
                            if (!StringUtils.isEmpty(item.getJavaImportType()) && importStr.indexOf(item.getJavaImportType()) == -1) {
                                importStr.append(item.getJavaImportType()).append("\n");
                            }
                        }).collect(Collectors.toList());
                Map<String, Object> map = Utils.beanToMap(tableColumnModel);
                map.put("columnList", columnList);
                map.put("requestType", requestType);
                map.put("package", packageStr);
                map.put("import", importStr);
                zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
                zipOutputStream.write(FreemarkerUtils.processIntoStream(requestTemp, map));
                zipOutputStream.closeEntry();
            }
        }
    }
}
