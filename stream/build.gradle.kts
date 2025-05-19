plugins {
    java
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":function"))
    implementation("org.springframework.cloud:spring-cloud-function-context")
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder")
    testImplementation("com.github.fppt:jedis-mock:${libs.versions.jedis.mock.get()}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
