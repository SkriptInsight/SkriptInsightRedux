plugins {
    `java-library`
    kotlin("jvm") version "1.6.10" apply false
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "io.github.skriptinsight.redux"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }

    dependencies {
        "implementation"(kotlin("stdlib"))
    }

    rootProject.dependencies {
        "implementation"(this@subprojects)
    }
}