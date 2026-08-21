package xyz.srnyx.manymobs.messages;

import org.jetbrains.annotations.NotNull;
import xyz.srnyx.annoyingapi.file.okaeri.ConfigBuilder;
import xyz.srnyx.annoyingapi.message.MessagesProvider;
import xyz.srnyx.manymobs.ManyMobs;


public class ManyMessagesProvider extends MessagesProvider {
    @NotNull private final ManyMobs plugin;

    public ManyMessagesProvider(@NotNull ManyMobs plugin) {
        this.plugin = plugin;

        defaults
                .prefix("&2&lMOBS &8&l| &a")
                .p("&a")
                .s("&2");
    }

    @Override @NotNull
    public ManyMobs getAnnoyingPlugin() {
        return plugin;
    }

    @Override
    public void mutateBuilder(@NotNull ConfigBuilder builder) {
        builder.config(messages = new ManyMessages(plugin));
    }

    @Override @NotNull
    public ManyMessages get() {
        return (ManyMessages) messages;
    }
}
