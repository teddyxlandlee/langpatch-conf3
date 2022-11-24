plugins {
    id ("com.github.johnrengelman.shadow") version "7.1.2"
}

architectury {
    platformSetupLoomIde()
    forge()
}

configurations {
    val common = maybeCreate("common")
    maybeCreate("shadowCommon")
    maybeCreate("compileClasspath").extendsFrom(common)
    maybeCreate("runtimeClasspath").extendsFrom(common)
    maybeCreate("developmentForge").extendsFrom(common)
}

object F7Constants {
    const val mcVersion = "1.17.1"
    const val forgeVersion = "1.17.1-37.1.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

dependencies {
    add("minecraft", "com.mojang:minecraft:${F7Constants.mcVersion}")
    add("forge", "net.minecraftforge:forge:${F7Constants.forgeVersion}")
    add("common", project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    // WARNING: 1.17 uses MojMaps (resources.ResourceLocation)
    // transformProductForge uses old srg
    add("shadowCommon", project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("META-INF/mods.toml") {
        expand("version" to project.version)
    }
}

tasks.shadowJar {
    exclude("fabric.mod.json", "architectury.common.json")
    configurations = listOf(project.configurations["shadowCommon"])
    archiveClassifier.set("dev-shadow")
}

tasks.remapJar {
    inputFile.set(tasks.shadowJar.get().archiveFile)
    dependsOn(tasks.shadowJar)
    archiveClassifier.set(null as String?)
}

tasks.jar {
    archiveClassifier.set("dev")
}

tasks.sourcesJar {
    val commonSources = project(":common").tasks.sourcesJar
    dependsOn(commonSources)
    from(commonSources.get().archiveFile.map { zipTree(it) })
}

(components["java"] as? AdhocComponentWithVariants)?.apply {
    withVariantsFromConfiguration(project.configurations["shadowRuntimeElements"]) {
        skip()
    }
}

publishing {
    publications {
        create("mavenForge", MavenPublication::class) {
            artifactId = "${rootProject.ext["archives_base_name"]}-${project.name}"
            from(components["java"])
        }
    }
    repositories {  }
}
