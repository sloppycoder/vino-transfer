val googleJavaFormatVersion = libs.versions.google.java.format.get()
val ktlintVersion: String = libs.versions.ktlint.get()
val javaVersion: String = libs.versions.java.get()
val jacocoToolVersion = libs.versions.jacoco.tool.get()

plugins {
    id("java-platform")
    alias(libs.plugins.spotless)
    alias(libs.plugins.dependency.management) apply (false)
    alias(libs.plugins.spring.boot) apply (false)
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
                googleJavaFormat(googleJavaFormatVersion).aosp().reflowLongStrings()
                formatAnnotations()
                trimTrailingWhitespace()
                endWithNewline()
            }

            kotlinGradle {
                target("*.gradle.kts")
                ktlint(ktlintVersion).editorConfigOverride(mapOf("indent_size" to "4"))
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
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(javaVersion))
            }
        }

        apply(plugin = "io.spring.dependency-management")
        extensions.configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
            imports {
                mavenBom("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}")
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.spring.cloud.get()}")
            }
        }

        configurations.named("compileOnly") {
            extendsFrom(configurations.getByName("annotationProcessor"))
        }

        apply(plugin = "jacoco")
    }

    plugins.withId("jacoco") {
        extensions.configure<JacocoPluginExtension> {
            toolVersion = jacocoToolVersion
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
        testLogging {
            events("passed", "failed", "skipped")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showStandardStreams = false
        }
    }
}
