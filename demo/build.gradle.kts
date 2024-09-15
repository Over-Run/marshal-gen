plugins {
    kotlin("jvm")
}

val jdkVersion: String by rootProject

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
}

tasks.register<JavaExec>("runGenTest") {
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = "io.github.overrun.marshalgen.test.TestKt"

    val runDir = project(":generated").projectDir.resolve("src/main/java/")
    workingDir = runDir

    javaLauncher = javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(jdkVersion))
    }

    doFirst {
        if (!runDir.exists()) {
            runDir.mkdirs()
        }
    }
}
