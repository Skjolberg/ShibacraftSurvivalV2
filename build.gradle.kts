plugins {
    java
}

group = "net.shibacraft"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}



dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT") // The Spigot API with no shadowing. Requires the OSS repo.
    compileOnly("me.clip:placeholderapi:2.10.10")
    compileOnly("net.luckperms:api:5.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}