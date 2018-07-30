${package}
${import}
/**
 * @author faster-builder
 */
@Service
@Transactional
@AllArgsConstructor
public class ${businessEnNameUpFirst}Service {
    private ${businessEnNameUpFirst}Mapper ${businessEnName}Mapper;

    /**
     * @param ${businessEnName} ${businessCnName}实体
     * @return ${businessCnName}分页列表
     */
    public PageInfo<${businessEnNameUpFirst}> page(${businessEnNameUpFirst} ${businessEnName}) {
        WeekendSqls<${businessEnNameUpFirst}> queryCondition = WeekendSqls.<${businessEnNameUpFirst}>custom()
            .andEqualTo(${businessEnNameUpFirst}::getDeleted, 0);
<#list columnList as item>
    <#if item.javaType=='String'>
        if (!StringUtils.isEmpty(${businessEnName}.get${item.columnNameHumpUpFirst}())) {
    <#else>
        if (${businessEnName}.get${item.columnNameHumpUpFirst}() != null) {
    </#if>
            queryCondition.andEqualTo(${businessEnNameUpFirst}::get${item.columnNameHumpUpFirst}, ${businessEnName}.get${item.columnNameHumpUpFirst}());
        }
</#list>
        return ${businessEnName}Mapper.selectPageByExample(${businessEnName}.rowBounds(), new Example.Builder(${businessEnNameUpFirst}.class)
            .where(queryCondition).orderByDesc("createDate")
            .build()).toPageInfo();
    }

    /**
     * @param id ${businessCnName}id
     * @return ${businessCnName}详情
     */
    public ${businessEnNameUpFirst} queryById(Long id) {
        return ${businessEnName}Mapper.selectByPrimaryKey(id);
    }

    /**
     * @param ${businessEnName} ${businessCnName}实体
     * @return ${businessCnName}详情
     */
    public ${businessEnNameUpFirst} query(${businessEnNameUpFirst} ${businessEnName}) {
        WeekendSqls<${businessEnNameUpFirst}> queryCondition = WeekendSqls.<${businessEnNameUpFirst}>custom()
            .andEqualTo(${businessEnNameUpFirst}::getDeleted, 0);
<#list columnList as item>
    <#if item.javaType=='String'>
        if (!StringUtils.isEmpty(${businessEnName}.get${item.columnNameHumpUpFirst}())) {
    <#else>
        if (${businessEnName}.get${item.columnNameHumpUpFirst}() != null) {
    </#if>
            queryCondition.andEqualTo(${businessEnNameUpFirst}::get${item.columnNameHumpUpFirst}, ${businessEnName}.get${item.columnNameHumpUpFirst}());
        }
</#list>
        return ${businessEnName}Mapper.selectOneByExample(new Example.Builder(${businessEnNameUpFirst}.class)
            .where(queryCondition)
            .build());
    }
}