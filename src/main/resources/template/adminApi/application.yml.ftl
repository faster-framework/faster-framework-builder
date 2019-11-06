spring:
  application:
    name: ${projectName}
  profiles:
    include:
      - web
      - mybatis
      - admin
app:
  group: ${dbName}