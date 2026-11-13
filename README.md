# Faster-Framework-Builder

Faster 系列框架的**代码生成器**：读取 MySQL 表结构，按模板生成接口 / 管理端 / 前端 / SQL 工程，并以 ZIP 下载。

- 在线 Demo：<https://builder.faster.org.cn/>
- 文档入口：<https://builder.faster.org.cn/>
- 最新版本：[![latest](https://badgen.net/github/release/faster-framework/faster-framework-builder?icon=github)](https://github.com/faster-framework/faster-framework-builder/releases/latest)
- 订阅发布通知：<https://github.com/faster-framework/faster-framework-builder/releases.atom>

## 2.0.0 技术栈

| 项目 | 版本 |
| --- | --- |
| JDK | 17+（LTS） |
| Spring Boot | 4.1.2 |
| Spring Framework | 7.0.10 |
| Jakarta EE | 11（`jakarta.validation`） |
| FreeMarker | 2.3.35 |
| 构建 | Maven 3.6.3+（仓库自带 `./mvnw`） |

> 1.x 系列（JDK 8 + Spring Boot 2.x + `javax`）已停止演进。2.0 不再依赖
> `cn.org.faster:spring-boot-starters`、MyBatis-Plus、Druid 与动态数据源，可独立构建运行。

## 快速开始

```bash
git clone https://github.com/faster-framework/faster-framework-builder.git
cd faster-framework-builder
./mvnw spring-boot:run          # 或 ./mvnw clean package && java -jar target/faster-framework-builder-2.0.0-SNAPSHOT.jar
```

打开 <http://127.0.0.1:8080/>，填写数据库连接与项目信息，点击「下载项目」。

只想生成 SQL、不需要连库时，直接调用接口：

```bash
curl -X POST http://127.0.0.1:8080/builder \
  -H 'Content-Type: application/json' \
  -d '{"type":5,"business":{"projectName":"demo"}}' \
  -o demo.zip
```

## 接口

### `GET /builder/options`

下发各生成类型的框架版本与骨架地址，前端据此渲染版本号，避免前后端版本漂移。

```json
[{"type":1,"version":"1.4.2.RELEASE","url":""},
 {"type":2,"version":"1.4.2.RELEASE","url":"https://github.com/.../v1.0.3.RELEASE.zip"}]
```

### `POST /builder`

| 字段 | 必填 | 说明 |
| --- | --- | --- |
| `type` | 是 | 1 接口、2 管理端接口、3 管理端前端、4 管理端+接口合一、5 SQL、6 CMS 接口、7 CMS 前端 |
| `business.projectName` | 是 | 项目名，用作下载文件名 |
| `business.basePackagePath` | 视类型 | type=1/2/4/6 必填，如 `com.example` |
| `business.tableName` | 否 | 留空表示生成整库 |
| `database` | 视类型 | type=1/2/3/4 必填：`{host, port, name, username, password}` |
| `dependency` | 否 | 覆盖服务端配置：`{version, url}` |

**成功**：`200` + `application/zip`，`Content-Disposition: attachment; filename*=UTF-8''<项目名>.zip`。

**失败**：JSON 错误体，且不会返回残缺的 ZIP：

```json
{"code":400,"message":"当前生成类型需要数据库连接信息"}
```

| code | 含义 |
| --- | --- |
| 400 | 参数校验失败 / 缺少数据库或包路径 / 类型不支持 |
| 404 | 指定的表不存在 |
| 502 | 连不上目标 MySQL，或官方骨架下载失败（响应里带具体 URL） |
| 500 | 模板渲染失败等内部错误 |

## 配置

全部配置项位于 `builder.*`（见 `src/main/resources/application.yml`，可用环境变量覆盖）：

| 配置 | 默认值 | 说明 |
| --- | --- | --- |
| `builder.jwt-base64-secret` | `ZmFzdGVyLWZyYW1ld29yaw==` | 生成单元测试用 JWT 的密钥，需与被生成工程一致 |
| `builder.jwt-env` | `local` | JWT 的 `env` claim |
| `builder.connect-timeout` | `5s` | 连接用户数据库超时 |
| `builder.query-timeout` | `15s` | 元数据查询超时 |
| `builder.skeleton-max-bytes` | `67108864` | 骨架 ZIP 体积上限 |
| `builder.skeleton-timeout` | `60s` | 骨架下载超时 |
| `builder.projects[]` | 内置 7 项 | `{type, version, url}`，覆盖后可指向自建骨架 |

## 架构

```
浏览器（static/index.html，Vue 3 + fetch）
        │ POST /builder (JSON)
        ▼
BuilderController ── 参数校验（jakarta.validation record）→ 返回 ResponseEntity<byte[]>
        ▼
BuilderService ── 校验 → MetadataReader 读取表/列 → 补全模型 → 选择 BuildContext
        ├── JdbcMetadataReader：纯 JDBC 读 information_schema（无连接池、无 ThreadLocal）
        ├── JwtTokenGenerator：JDK HmacSHA256 生成测试用 token
        └── SkeletonDownloader：JDK HttpClient 下载骨架，不落临时文件
        ▼
BuildContext（抽象）── 内存 ByteArrayOutputStream + EntryNormalizingZipOutputStream
        ▼
BuildStrategy（每个策略一类产物）── FreeMarker 渲染 → 写入 ZIP
```

关键约定：

- **不落地临时文件**：ZIP 全程在内存组装，异常时整体失败（不存在"半个项目"）。
- **条目名规范化**：去掉前导 `/`、统一 `/`、同名条目只写一次。
- **失败可见**：模板异常、下载失败、表不存在都会转换为 JSON 错误，不会被吞掉。
- **扩展方式不变**：新增产物 = 新增一个 `BuildStrategy` + `.ftl` + 在对应 `Context` 注册一行。

## 测试

```bash
./mvnw test
```

33 个用例，覆盖命名/类型推导、JWT、雪花 ID、ZIP 规范化、骨架合并、JDBC 参数校验、
**全部 29 个模板渲染**，以及 `POST /builder` 端到端（MockMvc + 打桩元数据）。

## 从 1.x 迁移到 2.0

| 变化 | 说明 |
| --- | --- |
| JDK 8 → 17 | `record`、`switch` 表达式、`text block`、`var`；Lombok 仍在用 |
| `javax.*` → `jakarta.*` | 仅影响本项目（模板生成的代码不含 servlet/validation 命名空间） |
| 移除 MyBatis-Plus + Druid + 动态数据源 | 改为纯 JDBC 读元数据，顺带消除连接/ThreadLocal 泄漏 |
| 移除对 `cn.org.faster` 运行期依赖 | `Utils.beanToMap`、`JwtService`、`BaseEntity` 用 JDK 原生实现替代 |
| 不再使用 `RestTemplate` | 改用 `HttpClient`，带超时与体积上限 |
| 响应契约统一 | Service 返回 `GeneratedArchive`，Controller 统一输出 200 + ZIP |
| 前端去依赖 | 去掉 jQuery / layer.js / Bootstrap CDN，仅保留 Vue 3 |

> 生成的项目仍指向 Faster 框架 1.4.x（其本身为 Boot 2.x）。待框架主线迁移到 Boot 4 后，
> 只需调整 `builder.projects[].version` 与模板中的 import 片段即可，生成器本身无需再改。

## 安全说明

`POST /builder` 会按用户提交的地址连接 MySQL，属于典型的 SSRF 面。当前已做：

- 主机名/端口/库名白名单式校验，账号密码通过 `Properties` 传递（不拼接进 URL）；
- 关闭 `allowLoadLocalInfile` / `allowMultiQueries`，强制 `sslMode=DISABLED` 仅用于读取元数据场景；
- 骨架下载限制协议、超时与体积。

**部署到公网时仍需在网关层加认证与限流**（如 Nginx Basic Auth + `limit_req`），
或仅在受信内网提供服务。

## 许可

[Apache License 2.0](LICENSE)
