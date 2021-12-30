plugins {
    application
    distribution
    //id("com.github.johnrengelman.shadow") version "7.1.1"
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation(project(":file"))
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j:0.12.0")
}

val lspMainClass = "io.github.skriptinsight.redux.lsp.LanguageServerEntrypointKt"
tasks {
    application {
        mainClass.set(lspMainClass)
    }
    installDist.get().mustRunAfter(jar.get())
    build.get().dependsOn(installDist.get())
    assemble.get().dependsOn(installDist.get())
    jar {
        manifest {
            attributes("Main-Class" to lspMainClass)
        }
    }
}