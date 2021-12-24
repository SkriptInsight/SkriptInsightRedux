plugins {
    kotlin("jvm") version "1.6.10" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "io.github.skriptinsight.redux"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        "implementation"(kotlin("stdlib"))
    }
}