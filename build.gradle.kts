import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.5.30"
    application
    id("com.bmuschko.tomcat") version "2.7.0"
}

group = "me.onexx"
version = "1.0"

application {
    mainClass.set("MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(
            listOf(
                "compileJava",
                "compileKotlin",
                "processResources"
            )
        ) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.5.30"))

    implementation("com.petersamokhin.vksdk:core:0.0.8")

    implementation("com.petersamokhin.vksdk:http-client-jvm-okhttp:0.0.8")

    implementation("org.mariadb.jdbc:mariadb-java-client:2.7.5")
    tomcat("org.apache.tomcat.embed:tomcat-embed-core:10.1.2")
    tomcat("org.apache.tomcat.embed:tomcat-embed-logging-juli:10.1.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}