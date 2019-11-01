spring:
  application:
    name: ${projectName}-admin-api
  profiles:
    include:
      - web
      - mybatis
      - admin