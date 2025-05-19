plugins {
    id("java-platform")
    id("com.diffplug.spotless") version "7.0.3"
    id("io.spring.dependency-management") version "1.1.7" apply (false)
    id("org.springframework.boot") version "3.4.5" apply (false)
}

allprojects {
    group = "net.vino9.vino.demo"
    version = "0.1.0-SNAPSHOT"

    apply(plugin = "com.diffplug.spotless")
    afterEvaluate {
        spotless {
            java {
                // Corrected target for Java files
                target("src/main/java/**/*.java", "src/test/java/**/*.java")
                importOrder()
                removeUnusedImports()
                googleJavaFormat("1.17.0").aosp().reflowLongStrings()
                formatAnnotations()
                trimTrailingWhitespace()
                endWithNewline()
            }

            kotlinGradle {
                target("*.gradle.kts")
                ktlint("1.5.0").editorConfigOverride(mapOf("indent_size" to "4"))
                trimTrailingWhitespace()
                endWithNewline()
            }

            format("misc") {
                target("*.gradle", "*.md", ".gitignore")
                trimTrailingWhitespace()
                endWithNewline()
            }
        }

        tasks.named("check") {
            dependsOn("spotlessApply")
        }
    }
}

subprojects {
    plugins.withType<JavaPlugin> {

        tasks.withType<JavaCompile>().configureEach {
            sourceCompatibility = "21"
            targetCompatibility = "21"
        }

        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of("21"))
            }
        }

        apply(plugin = "io.spring.dependency-management")
        extensions.configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
            imports {
                mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.5")
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.1")
            }
        }

        configurations.named("compileOnly") {
            extendsFrom(configurations.getByName("annotationProcessor"))
        }

        apply(plugin = "jacoco")
    }

    plugins.withId("jacoco") {
        extensions.configure<JacocoPluginExtension> {
            toolVersion = "0.8.13"
        }

        tasks.named<JacocoReport>("jacocoTestReport") {
            reports {
                html.required.set(true)
            }
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        finalizedBy("jacocoTestReport")
    }

    tasks.withType<Test>().configureEach {
        testLogging {
            events("passed", "failed", "skipped")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showStandardStreams = false
        }
    }
}
