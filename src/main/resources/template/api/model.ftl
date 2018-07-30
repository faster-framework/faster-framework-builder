${package}
${import}
/**
 * @author faster-builder
 * ${businessCnName} 模型
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "${tableName}")
public class ${businessEnNameUpFirst}Model{
<#list columnList as item>
    /**
     * ${item.columnComment}
     */
<#if item.isNullable=='NO'>
    <#if item.javaType=='String'>
    @NotBlank(message = "${item.columnComment}不可为空")
    <#else>
    @NotNull(message = "${item.columnComment}不可为空")
    </#if>
</#if>
    private ${item.javaType} ${item.columnNameHump};
</#list>
}