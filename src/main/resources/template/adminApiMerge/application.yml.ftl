spring:
  application:
    name: ${projectName}
  profiles:
    include:
      - web
      - mybatis
app:
  group: ${dbName}
  shiro:
    filter-chain-definition-map:
      "[/api/**]": anon