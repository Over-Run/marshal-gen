plugins {
    `java-library`
}

val marshalVersion: String by rootProject

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/releases")
}

dependencies {
    implementation("io.github.over-run:marshal:$marshalVersion")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}
