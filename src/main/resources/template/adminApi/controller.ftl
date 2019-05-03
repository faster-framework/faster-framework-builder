${package}
${import}
/**
 * @author faster-builder
 * ${businessCnName} Controller
 */
@RestController
@RequestMapping("/${businessEnName}")
@AllArgsConstructor
public class ${businessEnNameUpFirst}Controller {
    private ${businessEnNameUpFirst}Service ${businessEnName}Service;

    /**
     * ${businessCnName}分页列表
     *
     * @param request ${businessCnName}实体
     * @return ResponseEntity
     */
    @GetMapping
    @RequiresPermissions("${businessEnName}:list")
    public ResponseEntity list(${businessEnNameUpFirst}ListRequest request) {
        ${businessEnNameUpFirst} ${businessEnName} = new ${businessEnNameUpFirst}();
        BeanUtils.copyProperties(request, ${businessEnName});
        return ResponseEntity.ok(${businessEnName}Service.list(${businessEnName}));
    }

    /**
     * ${businessCnName}根据id查询详情
     *
     * @param id 主键id
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    @RequiresPermissions("${businessEnName}:info")
    public ResponseEntity queryById(@PathVariable Long id) {
        return ResponseEntity.ok(${businessEnName}Service.getById(id));
    }

    /**
     * ${businessCnName}根据条件查询详情
     *
     * @return ResponseEntity
     */
    @GetMapping("/query")
    @RequiresPermissions("${businessEnName}:info")
    public ResponseEntity query(${businessEnNameUpFirst}QueryRequest request) {
        ${businessEnNameUpFirst} ${businessEnName} = new ${businessEnNameUpFirst}();
        BeanUtils.copyProperties(request, ${businessEnName});
        return ResponseEntity.ok(${businessEnName}Service.query(${businessEnName}));
    }

    /**
     * 新增${businessCnName}
     *
     * @param request 请求参数
     * @return ResponseEntity
     */
    @PostMapping
    @RequiresPermissions("${businessEnName}:add")
    public ResponseEntity add(@Validated @RequestBody ${businessEnNameUpFirst}AddRequest request) {
        ${businessEnNameUpFirst} ${businessEnName} = new ${businessEnNameUpFirst}();
        BeanUtils.copyProperties(request, ${businessEnName});
        return ${businessEnName}Service.add(${businessEnName});
    }

    /**
     * 更新${businessCnName}
     *
     * @param request 请求参数
     * @param id 主键id
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    @RequiresPermissions("${businessEnName}:modify")
    public ResponseEntity update(@PathVariable Long id,@RequestBody ${businessEnNameUpFirst}UpdateRequest request) {
        ${businessEnNameUpFirst} ${businessEnName} = new ${businessEnNameUpFirst}();
        BeanUtils.copyProperties(request, ${businessEnName});
        ${businessEnName}.setId(id);
        return ${businessEnName}Service.update(${businessEnName});
    }

    /**
     * 删除${businessCnName}
     *
     * @param id 主键id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions("${businessEnName}:delete")
    public ResponseEntity delete(@PathVariable Long id) {
        return ${businessEnName}Service.delete(id);
    }
}