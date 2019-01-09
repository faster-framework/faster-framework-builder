spring:
  profiles:
    include:
      - web
      - mybatis
      - admin
    active: local
faster:
  project-name: ${projectName}-admin-api
  cluster-name: ${projectName}