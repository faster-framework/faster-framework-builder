spring:
  datasource:
    druid:
        url: jdbc:mysql://${dbHost}:${dbPort}/${dbName}?allowMultiQueries=true&useSSL=false
        username: ${dbUsername}
        password: ${dbEncryptPwd}
        public-key: ${dbPublicKey}
#faster:
#  upload:
#    local:
#      file-dir: {yourPath}
#      url-prefix: http://127.0.0.1:8080
mybatis:
 configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl