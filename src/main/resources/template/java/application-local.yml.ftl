app:
  datasource:
    name: ${dbName}
    host: ${dbHost}
    port: ${dbPort}
    username: ${dbUsername}
    password: ${dbPwd}
#  upload:
#    local:
#      file-dir: /data/upload/${dbName}
#      url-prefix: http://127.0.0.1:8080
logging:
  level:
    ${basePackagePath}: DEBUG