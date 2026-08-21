package xyz.srnyx.manymobs;

import eu.okaeri.configs.annotation.Comment;
import xyz.srnyx.annoyingapi.file.okaeri.RootConfig;
import xyz.srnyx.annoyingapi.stats.Stat;


public class ManyConfig extends RootConfig {
    @Comment("If false, no feedback message will be sent when using the spawn command")
    @Comment("This is useful if another plugin is using Many Mobs' spawn command and you want to reduce spam")
    @Stat
    public boolean enable_spawn_messages = true;
}
