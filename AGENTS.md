# AGENTS.md

本仓库的自动化协作约定。人类贡献者也可参考。

## 分支与远端

- 主分支：`master`，远端 `origin`（`git@github.com:faster-framework/faster-framework-builder.git`）。
- 默认直接在 `master` 上提交并推送，不额外开分支、不建 PR（除非任务明确要求）。

## 完成定义（Definition of Done）

一项编码任务只有同时满足以下条件才算完成：

1. `./mvnw -B -ntp verify` 通过（编译 + 全部测试，当前 33 个，含模板渲染与端到端）。
2. 改动涉及运行时行为时，至少手动验证一次真实 HTTP 调用（启动 `java -jar` 或 `spring-boot:run`）。
3. 提交信息说明「为什么」而不只是「改了什么」；破坏性变更标注 `BREAKING CHANGE`。
4. 已推送到 `origin/master`，且 `git status` 除本地临时文件外干净。

> 纯文档改动（README/AGENTS/注释）可不跑测试，但仍需提交并推送。

## 提交信息

遵循 Conventional Commits（`feat:`、`fix:`、`refactor:`、`test:`、`docs:`、`build:`、`chore:`），
中文正文，首行不超过 72 字符。破坏性变更在正文中写明迁移影响。

## 本地不提交的文件

- `INTRO.md`：本地静态分析报告，已在 `.git/info/exclude` 中排除。
- `target/`：构建产物。

## 技术约束

- 运行时：JDK 17+（CI 矩阵 17/21/25）、Spring Boot 4.1.2、Spring Framework 7.0.10、Jakarta EE 11。
- 禁止重新引入：JDK 8 语法、`javax.*` 包、MyBatis-Plus、Druid、动态数据源、`RestTemplate`、
  对 `cn.org.faster:spring-boot-starters` 的运行时依赖（会让生成器重新绑定到 Boot 2.x）。
- 新增产物类型的扩展方式：`BuildStrategy` 子类 + `src/main/resources/template/**/*.ftl` +
  在对应 `BuildContext#initBuildStrategy` 注册一行，并补充模板渲染测试。
- 模板异常必须向上抛出（`BuilderException`），不允许 `printStackTrace` 后继续返回 ZIP。
