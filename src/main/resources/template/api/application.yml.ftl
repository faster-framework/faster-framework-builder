spring:
  profiles:
    include:
      - web
      - mybatis
    active: local
faster:
  project-name: ${projectName}-api
  cluster-name: ${projectName}