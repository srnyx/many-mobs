package xyz.srnyx.manymobs;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;


public class ManyMobs extends AnnoyingPlugin {
    public ManyMobs() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("many-mobs"),
                        PluginPlatform.hangar(this, "srnyx"),
                        PluginPlatform.spigot("109423")))
                .bStatsOptions(bStatsOptions -> bStatsOptions.id(18669))
                .registrationOptions.commandsToRegister.add(new ManymobsCommand(this));
    }
}
