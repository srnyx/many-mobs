package xyz.srnyx.manymobs;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.AnnoyingMessage;
import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


public class ManymobsCommand implements AnnoyingCommand {
    @NotNull private final ManyMobs plugin;

    @Contract(pure = true)
    public ManymobsCommand(@NotNull ManyMobs plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public ManyMobs getPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "manymobs.command";
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        final String[] args = sender.getArgs();

        // reload
        if (args.length == 1 && sender.argEquals(0, "reload")) {
            plugin.reloadPlugin();
            new AnnoyingMessage(plugin, "command.reload").send(sender);
            return;
        }

        // spawn [<type>] [<amount>]
        if (args.length >= 2 && sender.argEquals(0, "spawn")) {
            if (!(sender.getCmdSender() instanceof Player)) {
                new AnnoyingMessage(plugin, "error.player-only").send(sender);
                return;
            }

            // Get EntityType
            final EntityType type;
            try {
                type = EntityType.valueOf(args[1].toUpperCase());
            } catch (final IllegalArgumentException e) {
                new AnnoyingMessage(plugin, "error.invalid-argument")
                        .replace("%argument%", args[1])
                        .send(sender);
                return;
            }
            final Player player = sender.getPlayer();
            int amount = 1;
            Location location = player.getLocation();

            // spawn [<type>] [<amount>]
            if (args.length >= 3) {
                // Get amount
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (final NumberFormatException e) {
                    new AnnoyingMessage(plugin, "error.invalid-argument")
                            .replace("%argument%", args[2])
                            .send(sender);
                    return;
                }

                // spawn [<type>] [<amount>] [<x>] [<y>] [<z>]
                if (args.length >= 6) {
                    // Get location
                    try {
                        location = new Location(player.getWorld(), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                    } catch (final NumberFormatException e) {
                        new AnnoyingMessage(plugin, "error.invalid-arguments").send(sender);
                        return;
                    }

                    // spawn [<type>] [<amount>] [<x>] [<y>] [<z>] [<yaw>] [<pitch>]
                    if (args.length == 8) {
                        // Set location's yaw and pitch
                        try {
                            location.setYaw(Float.parseFloat(args[6]));
                            location.setPitch(Float.parseFloat(args[7]));
                        } catch (final NumberFormatException e) {
                            new AnnoyingMessage(plugin, "error.invalid-arguments").send(sender);
                            return;
                        }
                    }
                }
            }

            // Spawn entities
            for (int i = 0; i < amount; i++) player.getWorld().spawnEntity(location, type);
            new AnnoyingMessage(plugin, "command.spawn")
                    .replace("%amount%", amount)
                    .replace("%type%", type.name())
                    .replace("%location%", location.getX() + ", " + location.getY() + ", " + location.getZ() + ", " + location.getYaw() + ", " + location.getPitch())
                    .send(sender);
            return;
        }

        new AnnoyingMessage(plugin, "error.invalid-arguments").send(sender);
    }

    @Override @Nullable
    public Collection<String> onTabComplete(@NotNull AnnoyingSender sender) {
        final String[] args = sender.getArgs();

        // <reload|spawn>
        if (args.length == 1) return Arrays.asList("reload", "spawn");

        // spawn
        if (sender.argEquals(0, "spawn")) {
            final Location location = sender.getPlayer().getLocation();

            // spawn [<type>]
            if (args.length == 2) return Arrays.stream(EntityType.values())
                    .map(EntityType::name)
                    .collect(Collectors.toList());
            // spawn [<type>] [<amount>]
            if (args.length == 3) return Collections.singleton("[<amount>]");
            // spawn [<type>] [<amount>] [<x>]
            if (args.length == 4) return Collections.singleton(String.valueOf(location.getX()));
            // spawn [<type>] [<amount>] [<x>] [<y>]
            if (args.length == 5) return Collections.singleton(String.valueOf(location.getY()));
            // spawn [<type>] [<amount>] [<x>] [<y>] [<z>]
            if (args.length == 6) return Collections.singleton(String.valueOf(location.getZ()));
            // spawn [<type>] [<amount>] [<x>] [<y>] [<z>] [<yaw>]
            if (args.length == 7) return Collections.singleton(String.valueOf(location.getYaw()));
            // spawn [<type>] [<amount>] [<x>] [<y>] [<z>] [<yaw>] [<pitch>]
            if (args.length == 8) return Collections.singleton(String.valueOf(location.getPitch()));
        }

        return null;
    }
}
