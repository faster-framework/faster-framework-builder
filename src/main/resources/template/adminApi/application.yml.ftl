spring:
  application:
    name: ${projectName}-admin-api
  profiles:
    include:
      - web
      - mybatis
      - admin
    active: local
faster:
  cluster-name: ${projectName}