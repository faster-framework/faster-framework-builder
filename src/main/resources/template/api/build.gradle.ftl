plugins {
    id 'java'
    id 'org.springframework.boot' version '2.0.2.RELEASE'
}

group '${basePackagePath}'
version '1.0-SNAPSHOT'

sourceCompatibility = 1.8
sourceSets.main.resources.srcDirs = ["src/main/java", "src/main/resources"]
repositories {
    mavenCentral()
    maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
}

dependencies {
    compile 'cn.org.faster:faster-framework-core-spring-boot-starter:${dependencyVersion}'
}