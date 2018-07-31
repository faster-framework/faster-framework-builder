${package}
${import}
/**
 * @author faster-builder
 * ${businessCnName} Service
 */
@Service
@Transactional
@AllArgsConstructor
public class ${businessEnNameUpFirst}Service {
    private ${businessEnNameUpFirst}Mapper ${businessEnName}Mapper;

    /**
     * 分页查询
     * @param ${businessEnName} 请求参数
     * @return ${businessCnName}分页列表
     */
    public PageInfo<${businessEnNameUpFirst}> list(${businessEnNameUpFirst} ${businessEnName}) {
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
     * 根据主键id查询详情
     * @param id ${businessCnName}id
     * @return ${businessCnName}详情
     */
    public ${businessEnNameUpFirst} queryById(Long id) {
        return ${businessEnName}Mapper.selectByPrimaryKey(id);
    }

    /**
     * 根据条件查询详情
     * @param ${businessEnName} 请求参数
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

    /**
    * 添加${businessCnName}
    * @param ${businessEnName} 实体
    * @return ResponseEntity
    */
    public ResponseEntity add(${businessEnNameUpFirst} ${businessEnName}) {
        ${businessEnName}.preInsert();
        ${businessEnName}Mapper.insertSelective(${businessEnName});
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
    * 修改${businessCnName}
    * @param ${businessEnName} 实体
    * @return ResponseEntity
    */
    public ResponseEntity update(${businessEnNameUpFirst} ${businessEnName}) {
        ${businessEnName}.preUpdate();
        ${businessEnName}Mapper.updateByPrimaryKeySelective(${businessEnName});
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 删除${businessCnName}
     * @param id 主键id
     * @return ResponseEntity
     */
    public ResponseEntity delete(Long id) {
        ${businessEnNameUpFirst} delete = new ${businessEnNameUpFirst}();
        delete.setId(id);
        delete.setDeleted(1);
        delete.preUpdate();
        ${businessEnName}Mapper.updateByPrimaryKeySelective(delete);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}