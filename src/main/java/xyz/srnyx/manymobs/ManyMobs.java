package xyz.srnyx.manymobs;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;


public class ManyMobs extends AnnoyingPlugin {
    @NotNull public ManyConfig config = new ManyConfig(this);

    public ManyMobs() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("p7CONpnK"),
                        PluginPlatform.hangar(this, "srnyx"),
                        PluginPlatform.spigot("109423")))
                .bStatsOptions(bStatsOptions -> bStatsOptions.id(18669))
                .registrationOptions.toRegister.add(new ManymobsCommand(this));
    }

    @Override
    public void reload() {
        config = new ManyConfig(this);
    }
}
