package xyz.srnyx.manymobs;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.file.AnnoyingResource;


public class ManyConfig {
    public final boolean enableSpawnMessages;

    public ManyConfig(@NotNull ManyMobs plugin) {
        enableSpawnMessages = new AnnoyingResource(plugin, "config.yml").getBoolean("enable-spawn-messages", true);
    }
}
