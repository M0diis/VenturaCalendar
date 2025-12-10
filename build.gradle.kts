plugins {
    java
    id("com.gradleup.shadow") version "8.3.5"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "me.M0dii"
version = "2.6.6"

tasks.shadowJar {
    relocate("org.bstats", "me.m0dii.venturacalendar")
    minimize()
    archiveFileName.set("VenturaCalendar-$version.jar")
}

tasks.processResources {
    filesMatching("**/*.yml") {
        expand("version" to version)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven { url = uri("https://ci.ender.zone/plugin/repository/everything/") }
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        content {
            includeGroup("org.bukkit")
            includeGroup("org.spigotmc")
        }
    }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/central") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")

    // compileOnly("org.spigotmc:spigot:1.19.2-R0.1-SNAPSHOT") // Only local
    // compileOnly("com.arcaniax:HeadDatabase-API:1.3.1")
    compileOnly("me.clip:placeholderapi:2.11.6")
    implementation("org.bstats:bstats-bukkit:2.2.1")
    implementation("com.github.cryptomorin:XSeries:8.6.1")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    // https://github.com/PlaceholderAPI/PlaceholderAPI
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    runServer {
        downloadPlugins {
            modrinth("viaversion", "5.5.0-SNAPSHOT+793")
            modrinth("viabackwards", "5.4.2")
        }
        minecraftVersion("1.21.8")
    }
}