${package}
${import}
/**
 * @author faster-builder
 * ${businessCnName} request
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ${businessEnNameUpFirst}${requestType} extends BaseEntity{
<#list columnList as item>
    /**
     * ${item.columnComment}
     */
    private ${item.javaType} ${item.columnNameHump};
</#list>
}