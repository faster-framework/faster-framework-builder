${package}
${import}
/**
 * @author faster-builder
 * ${businessCnName} Test
 */
public class ${businessEnNameUpFirst}Test extends BaseTest{
    /**
     * 生成的为local环境的token。
     * 通过jwtService.createToken创建。其中audience设为0L，expSecond不超时，base64Security使用ProjectProperties中的默认值，env为local。
     */
    @Override
    public String getToken() {
        return "${headerToken}";
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =  MockMvcRequestBuilders.get("/v1/${businessEnName}");
        mockHttpServletRequestBuilder.param("pageSize","10");
        mockHttpServletRequestBuilder.param("pageNumber","1");
        <#list columnList as item>
        mockHttpServletRequestBuilder.param("${item.columnNameHump}","");
        </#list>
        this.buildRequest(() -> mockHttpServletRequestBuilder).andExpect(status().is2xxSuccessful());
    }

    /**
     * 根据id查询
     */
    @Test
    public void queryById() throws Exception {
        this.buildRequest(() -> MockMvcRequestBuilders.get("/v1/${businessEnName}/{id}", "")).andExpect(status().is2xxSuccessful());
    }

    /**
     * 根据参数查询
     */
    @Test
    public void query() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =  MockMvcRequestBuilders.get("/v1/${businessEnName}/query");
        <#list columnList as item>
        mockHttpServletRequestBuilder.param("${item.columnNameHump}","");
        </#list>
        this.buildRequest(() -> mockHttpServletRequestBuilder).andExpect(status().is2xxSuccessful());
    }

    /**
     * 添加
     */
    @Test
    public void add() throws Exception {
        ${businessEnNameUpFirst}AddRequest request = new ${businessEnNameUpFirst}AddRequest();
        <#list columnList as item>
        request.set${item.columnNameHumpUpFirst}(null);
        </#list>
        this.buildRequest(() -> MockMvcRequestBuilders.post("/v1/${businessEnName}").content(JSON.toJSONString(request))).andExpect(status().is2xxSuccessful());
    }

    /**
     * 更新
     */
    @Test
    public void update() throws Exception {
        ${businessEnNameUpFirst}UpdateRequest request = new ${businessEnNameUpFirst}UpdateRequest();
        <#list columnList as item>
        request.set${item.columnNameHumpUpFirst}(null);
        </#list>
        this.buildRequest(()-> MockMvcRequestBuilders.put("/v1/${businessEnName}/{id}","").content(JSON.toJSONString(request))).andExpect(status().is2xxSuccessful());
    }

    /**
     * 删除
     */
    @Test
    public void delete() throws Exception {
        this.buildRequest(()-> MockMvcRequestBuilders.delete("/v1/${businessEnName}/{id}","")).andExpect(status().is2xxSuccessful());
    }

}