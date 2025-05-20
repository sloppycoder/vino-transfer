rootProject.name = "vino-transfer"
include(
    ":function",
    ":api",
    ":stream",
)

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("web")
