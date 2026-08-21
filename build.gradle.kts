plugins {
    java
    id("xyz.srnyx.gradle-galaxy") version "418a1ba"
    id("com.gradleup.shadow") version "9.6.1"
    id("me.modmuss50.mod-publish-plugin") version "675051c"
    id("io.papermc.hangar-publish-plugin") version "0.1.4"
    id("xyz.jpenilla.run-paper") version "3.1.0"
}

group = "xyz.srnyx"
description = "Adds a command to summon multiple mobs at once"

galaxy {
    minecraft {
        spigotAPI("1.8.8")
        annoyingAPI("8402640")

        pluginYml {
            developerData(SRNYX)

            command("manymobs") {
                aliases.addAll("mobs", "spawnmany", "summonmany")
                description = "Main command for Many Mobs"

                permission("command")
            }
        }

        platformPublishing {
            github("srnyx/many-mobs")
            modrinth("p7CONpnK")
            hangar("ManyMobs")
            spigot("109423")
            curseforge("897250")

            projectData("many-mobs")
        }
    }
}
