plugins {
    id("com.github.johnrengelman.shadow") version "7.1.1"
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}


dependencies {
    implementation(project(":node-analysis"))
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j:0.13.0-SNAPSHOT")
}