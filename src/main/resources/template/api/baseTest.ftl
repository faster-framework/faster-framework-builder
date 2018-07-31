${package}
${import}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public abstract class BaseTest {
    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Before
    public void setup() {
        DefaultMockMvcBuilder builder = webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }


    public ResultActions buildRequest(Supplier<MockHttpServletRequestBuilder> method) throws Exception {
        return this.mockMvc.perform(method.get().characterEncoding(StandardCharsets.UTF_8.name())
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, getBaseHeader())).andDo(print());
    }

    public abstract String getBaseHeader();
}
