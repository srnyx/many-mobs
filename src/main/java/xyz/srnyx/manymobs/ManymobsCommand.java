package xyz.srnyx.manymobs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.utility.BukkitUtility;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class ManymobsCommand implements AnnoyingCommand {
    @NotNull private final ManyMobs plugin;

    public ManymobsCommand(@NotNull ManyMobs plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public ManyMobs getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "manymobs.command";
    }

    @Override @NotNull
    public Predicate<String[]> getArgsPredicate() {
        return args -> args.length != 0;
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        final String[] args = sender.args;

        // reload
        if (args.length == 1 && sender.argEquals(0, "reload")) {
            plugin.reloadPlugin();
            new AnnoyingMessage(plugin, "command.reload").send(sender);
            return;
        }

        // spawn [<type>] [<amount>]
        if (args.length < 2 || !sender.argEquals(0, "spawn")) {
            sender.invalidArguments();
            return;
        }
        if (!sender.checkPlayer()) return;

        // Get EntityType
        final EntityType type;
        try {
            type = EntityType.valueOf(args[1].toUpperCase());
        } catch (final IllegalArgumentException e) {
            sender.invalidArgument(args[1]);
            return;
        }

        final Player player = sender.getPlayer();
        int amount = 1;
        Location location = player.getLocation();
        if (args.length >= 3) {
            // Get amount
            try {
                amount = Integer.parseInt(args[2]);
            } catch (final NumberFormatException e) {
                sender.invalidArgument(args[2]);
                return;
            }

            // Get location
            location = getLocation(sender);
            if (location == null) return;
        }

        // Spawn entities
        for (int i = 0; i < amount; i++) player.getWorld().spawnEntity(location, type);
        new AnnoyingMessage(plugin, "command.spawn")
                .replace("%amount%", amount)
                .replace("%type%", type.name())
                .replace("%location%", location.getX() + ", " + location.getY() + ", " + location.getZ() + ", " + location.getYaw() + ", " + location.getPitch())
                .send(sender);
    }

    @Override @Nullable
    public Collection<String> onTabComplete(@NotNull AnnoyingSender sender) {
        final String[] args = sender.args;

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
            // spawn [<type>] [<amount>] [<x|player>]
            if (args.length == 4) {
                final List<String> results = new ArrayList<>(BukkitUtility.getOnlinePlayerNames());
                results.add(0, String.valueOf(location.getX()));
                return results;
            }
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

    @Nullable
    private Location getLocation(@NotNull AnnoyingSender sender) {
        final String[] args = sender.args;

        // spawn [<type>] [<amount>] [<player>]
        if (args.length == 4) {
            // Get player
            final Player target = Bukkit.getPlayer(args[3]);
            if (target == null) {
                sender.invalidArgument(args[3]);
                return null;
            }
            return target.getLocation();
        }

        // spawn [<type>] [<amount>] [<x>] [<y>] [<z>]
        if (args.length < 6) return null;
        final Location location;
        try {
            location = new Location(sender.getPlayer().getWorld(), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
        } catch (final NumberFormatException e) {
            sender.invalidArguments();
            return null;
        }

        // spawn [<type>] [<amount>] [<x>] [<y>] [<z>] [<yaw>] [<pitch>]
        if (args.length == 8) try {
            location.setYaw(Float.parseFloat(args[6]));
            location.setPitch(Float.parseFloat(args[7]));
        } catch (final NumberFormatException e) {
            sender.invalidArguments();
            return null;
        }

        return location;
    }
}
