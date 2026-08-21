package xyz.srnyx.manymobs.messages;

import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Include;
import eu.okaeri.configs.annotation.IncludePosition;
import eu.okaeri.validator.annotation.NotNull;
import xyz.srnyx.annoyingapi.file.okaeri.SubConfig;
import xyz.srnyx.annoyingapi.message.AnnoyingMessages;
import xyz.srnyx.annoyingapi.message.json.message.JsonChatMessage;
import xyz.srnyx.manymobs.ManyMobs;


@Include(value = AnnoyingMessages.class, position = IncludePosition.BEFORE)
public class ManyMessages extends AnnoyingMessages {
    public ManyMessages(@org.jetbrains.annotations.NotNull ManyMobs annoyingPlugin) {
        super(annoyingPlugin);
    }

    @Comment
    @NotNull public Command command = new Command(this);

    public static class Command extends SubConfig<ManyMessages, ManyMessages> {
        public Command(@org.jetbrains.annotations.NotNull ManyMessages parent) {
            super(parent);
        }

        @NotNull public JsonChatMessage reload = getRoot().defaultMessage("%prefix%Successfully reloaded the config@@%p%%command%@@%command%");

        @Comment
        @Comment("Placeholders: %amount%, %type%, %world%, %x==number%, %y==number%, %z==number%, %yaw==number%, %pitch==number%, %location% (x, y, z, yaw, pitch)")
        @NotNull public JsonChatMessage spawn = getRoot().defaultMessage("%prefix%Spawned %s%%amount% %type%%p%s in %s%%world%%p% at %s%%x==#0.0%%p%, %s%%y==#0.0%%p%, %s%%z==#0.0%%p%, %s%%yaw==#0.0%%p%, %s%%pitch==#0.0%%p%@@%p%%command%@@%command%");

        @Comment
        @Comment("Placeholders: %amount%, %type%, %locations%")
        @NotNull public JsonChatMessage spawn_multiple = getRoot().defaultMessage("%prefix%Spawned %s%%amount% %type%%p%s at %s%%locations%%p% locations@@%p%%command%@@%command%");
    }
}
