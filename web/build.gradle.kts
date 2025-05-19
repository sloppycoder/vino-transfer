plugins {
    java
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":function"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.github.fppt:jedis-mock:${libs.versions.jedis.mock.get()}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
