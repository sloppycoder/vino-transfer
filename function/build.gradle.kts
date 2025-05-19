plugins {
    java
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-function-web")
    implementation("org.springframework.cloud:spring-cloud-function-context")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.github.fppt:jedis-mock:${libs.versions.jedis.mock.get()}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
