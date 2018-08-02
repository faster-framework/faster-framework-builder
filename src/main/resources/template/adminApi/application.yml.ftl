spring:
  profiles:
    include:
      - web
      - mybatis
      - admin
    active: dev

---
spring:
  profiles: dev
  datasource:
    druid:
        url: jdbc:mysql://${dbHost}:${dbPort}/${dbName}?allowMultiQueries=true&useSSL=false
        username: ${dbUsername}
        password: ${dbPwd}
#faster:
#  upload:
#    local:
#      file-dir: {yourPath}
#      url-prefix: http://127.0.0.1:8080
mybatis:
 configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

---
spring:
  profiles: test
  datasource:
    druid:
        url: jdbc:mysql://${dbHost}:${dbPort}/${dbName}?allowMultiQueries=true&useSSL=false
        username: ${dbUsername}
        password: ${dbPwd}

#faster:
#  upload:
#    local:
#      file-dir: {yourPath}
#      url-prefix: http://127.0.0.1:8080

---
spring:
  profiles: prod
  datasource:
    druid:
        url: jdbc:mysql://${dbHost}:${dbPort}/${dbName}?allowMultiQueries=true&useSSL=false
        username: ${dbUsername}
        password: ${dbPwd}
#faster:
#  upload:
#    local:
#      file-dir: {yourPath}
#      url-prefix: http://127.0.0.1:8080
