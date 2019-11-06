spring:
  application:
    name: ${projectName}
  profiles:
    include:
      - web
      - mybatis
app:
  group: ${dbName}