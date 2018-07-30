${package}
${import}
/**
 * @author faster-builder
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "${tableName}")
public class ${businessEnNameUpFirst}Entity extends BaseEntity{
<#list columnList as item>
    /**
     * ${item.columnComment}
     */
    private ${item.javaType} ${item.columnNameHump};
</#list>
}