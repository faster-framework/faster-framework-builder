spring:
  application:
    name: ${projectName}-api
  profiles:
    include:
      - web
      - mybatis
    active: local
faster:
  cluster-name: ${projectName}