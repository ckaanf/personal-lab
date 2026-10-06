plugins {
    // JDK 21이 없는 PC에서도 Gradle이 알아서 내려받는다
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "timeout-lab"
