plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "business1"
include("src:main:email-worker")
findProject(":src:main:email-worker")?.name = "email-worker"
