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

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<Javadoc> {
    options {
        if (this is CoreJavadocOptions) {
            // TODO
            addBooleanOption("-enable-preview", true)
            addStringOption("source", "23")
            if (this is StandardJavadocDocletOptions) {
                links("https://over-run.github.io/memstack/", "https://over-run.github.io/marshal/")
            }
        }
        jFlags("-Dstdout.encoding=UTF-8", "-Dstderr.encoding=UTF-8")
    }
}
