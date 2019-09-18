spring:
  datasource:
    druid:
        url: jdbc:mysql://${dbHost}:${dbPort}/${dbName}?allowMultiQueries=true&useSSL=false
        username: ${dbUsername}
        password: ${dbPwd}
#faster:
#  upload:
#    local:
#      file-dir: /data/upload/${dbName}
#      url-prefix: http://127.0.0.1:8080