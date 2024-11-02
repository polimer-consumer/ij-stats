plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0-Beta2"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.polimerconsumer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.2")
    type.set("IC")
    plugins.set(listOf("org.jetbrains.kotlin"))
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler:2.1.0-Beta2") // Kotlin PSI
    implementation(kotlin("stdlib"))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("232.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
