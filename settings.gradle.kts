rootProject.name = "vino-transfer"
include(
    ":function",
)

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("web")
