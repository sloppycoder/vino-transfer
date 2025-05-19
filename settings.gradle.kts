rootProject.name = "vino-transfer"
include(
    ":function",
    ":web",
    ":stream",
)

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("web")
