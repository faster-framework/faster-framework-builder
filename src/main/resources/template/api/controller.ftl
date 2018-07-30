${package}
${import}
/**
 * @author faster-builder
 * ${businessCnName} Controller
 */
@RestController
@RequestMapping("/{version}/${businessEnName}")
@AllArgsConstructor
public class ${businessEnNameUpFirst}Controller {
    private ${businessEnNameUpFirst}Service ${businessEnName}Service;

    /**
     * ${businessCnName}分页列表
     *
     * @param ${businessEnName} ${businessCnName}实体
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity list(${businessEnNameUpFirst} ${businessEnName}) {
        return ResponseEntity.ok(${businessEnName}Service.list(sysRole));
    }

    /**
     * ${businessCnName}根据id查询详情
     *
     * @param id 主键id
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity queryById(@PathVariable Long id) {
        return ResponseEntity.ok(${businessEnName}Service.queryById(id));
    }

    /**
     * ${businessCnName}根据条件查询详情
     *
     * @param id 主键id
     * @return ResponseEntity
     */
    @GetMapping("/query")
    public ResponseEntity query(${businessEnNameUpFirst} ${businessEnName}) {
        return ResponseEntity.ok(${businessEnName}Service.query(${businessEnName}));
    }

    /**
     * 新增${businessCnName}
     *
     * @param request 请求参数
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity add(@Validated @RequestBody ${businessEnNameUpFirst}Model request) {
        return ${businessEnName}Service.add(request);
    }

    /**
     * 更新${businessCnName}
     *
     * @param request 请求参数
     * @param id 主键id
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody ${businessEnNameUpFirst}Model request, @PathVariable Long id) {
        return ${businessEnName}Service.update(request, id);
    }

    /**
     * 删除${businessCnName}
     *
     * @param id 主键id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return ${businessEnName}Service.delete(id);
    }
}