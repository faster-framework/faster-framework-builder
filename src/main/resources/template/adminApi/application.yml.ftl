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
      "[/admin/login]": anon
      "[/admin/media/**]": anon
      "[/admin/captcha/**]": anon
      "[/admin/**]": jwt