import xyz.srnyx.gradlegalaxy.data.config.DependencyConfig
import xyz.srnyx.gradlegalaxy.data.config.JavaSetupConfig
import xyz.srnyx.gradlegalaxy.utility.setupAnnoyingAPI
import xyz.srnyx.gradlegalaxy.utility.spigotAPI


plugins {
    java
    id("xyz.srnyx.gradle-galaxy") version "2.0.2"
    id("com.gradleup.shadow") version "8.3.9"
}

spigotAPI(config = DependencyConfig(version = "1.8.8"))
setupAnnoyingAPI(
    javaSetupConfig = JavaSetupConfig(
        group = "xyz.srnyx",
        version = "2.0.0",
        description = "Adds a command to summon multiple mobs at once"),
    annoyingAPIConfig = DependencyConfig(version = "e9ad7a91ef"))
