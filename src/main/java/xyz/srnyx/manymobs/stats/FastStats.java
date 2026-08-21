package xyz.srnyx.manymobs.stats;

import dev.faststats.Metrics;
import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.stats.loader.FastStatsLoader;
import xyz.srnyx.manymobs.ManyMobs;


public class FastStats extends FastStatsLoader {
    @NotNull private final ManyMobs plugin;

    public FastStats(@NotNull ManyMobs plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public ManyMobs getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getId() {
        return "8d0e5bec70cabab3d0d4e955bbbe8b26";
    }

    @Override
    public void mutateMetricsFactory(@NotNull Metrics.Factory factory) {
        factory.addMetric(config("config", () -> plugin.config));
    }
}
