package xyz.srnyx.manymobs;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.manymobs.messages.ManyMessagesProvider;
import xyz.srnyx.manymobs.stats.FastStats;


public class ManyMobs extends AnnoyingPlugin {
    public ManyConfig config;

    public ManyMobs() {
        options.statsOptions(statsOptions -> statsOptions
                .bStats(bStatsOptions -> bStatsOptions.id(18669))
                .fastStats(fastStatsOptions -> fastStatsOptions.loader(FastStats.class)));
    }

    @Override @NotNull
    public ManyMessagesProvider getMessages() {
        return (ManyMessagesProvider) super.getMessages();
    }

    @Override
    public void load() {
        config = configLoader.build(builder -> builder.config(new ManyConfig()));
    }

    @Override
    public void reload() {
        config.reload();
    }
}
