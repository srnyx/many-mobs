package xyz.srnyx.manymobs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.command.selector.SelectorOptional;
import xyz.srnyx.annoyingapi.libs.javautilities.StringUtility;
import xyz.srnyx.annoyingapi.libs.javautilities.manipulation.Mapper;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.message.DefaultReplaceType;
import xyz.srnyx.annoyingapi.utility.BukkitUtility;

import java.util.*;
import java.util.stream.Collectors;


public class ManymobsCommand extends AnnoyingCommand {
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

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        // reload
        if (sender.args.length == 1 && sender.argEquals(0, "reload")) {
            plugin.reloadPlugin();
            new AnnoyingMessage(plugin, "command.reload").send(sender);
            return;
        }

        // spawn [<type>] [<amount>]
        if (sender.args.length < 2 || !sender.argEquals(0, "spawn")) {
            sender.invalidArguments();
            return;
        }

        // Get EntityType
        final EntityType type = sender
                .getArgumentOptionalFlat(1, arg -> Mapper.toEnum(arg, EntityType.class))
                .orElse(null);
        if (type == null) return;

        // Get amount
        Integer amount = 1;
        if (sender.args.length >= 3) {
            amount = sender.getArgumentOptionalFlat(2, Mapper::toInt).orElse(null);
            if (amount == null) return;
            if (amount <= 0) {
                sender.invalidArgument(amount);
                return;
            }
        }

        // Get locations
        final List<Location> locations;
        if (sender.args.length >= 4) {
            locations = getLocations(sender);
            if (locations == null) return;
        } else {
            if (!sender.checkPlayer()) return;
            locations = Collections.singletonList(sender.getPlayer().getLocation());
        }

        // Spawn entities
        for (final Location location : locations) {
            final World world = location.getWorld();
            for (int i = 0; i < amount; i++) world.spawnEntity(location, type);
        }

        // Send message
        if (!plugin.config.enableSpawnMessages) return;
        if (locations.size() == 1) {
            final Location location = locations.get(0);
            final double x = location.getX();
            final double y = location.getY();
            final double z = location.getZ();
            final float yaw = location.getYaw();
            final float pitch = location.getPitch();
            new AnnoyingMessage(plugin, "command.spawn")
                    .replace("%amount%", amount)
                    .replace("%type%", type.name())
                    .replace("%world%", location.getWorld().getName())
                    .replace("%x%", x, DefaultReplaceType.NUMBER)
                    .replace("%y%", y, DefaultReplaceType.NUMBER)
                    .replace("%z%", z, DefaultReplaceType.NUMBER)
                    .replace("%yaw%", yaw, DefaultReplaceType.NUMBER)
                    .replace("%pitch%", pitch, DefaultReplaceType.NUMBER)
                    .replace("%location%", x + ", " + y + ", " + z + ", " + yaw + ", " + pitch)
                    .send(sender);
        } else {
            new AnnoyingMessage(plugin, "command.spawn-multiple")
                    .replace("%amount%", amount)
                    .replace("%type%", type.name())
                    .replace("%locations%", locations.size())
                    .send(sender);
        }
    }

    @Override @Nullable
    public Collection<String> onTabComplete(@NotNull AnnoyingSender sender) {
        // reload, spawn
        if (sender.args.length == 1) return Arrays.asList("reload", "spawn");

        // spawn
        if (sender.argEquals(0, "spawn")) {
            final Location location = sender.getPlayer().getLocation();

            // type
            if (sender.args.length == 2) return Arrays.stream(EntityType.values())
                    .map(EntityType::name)
                    .collect(Collectors.toList());
            // amount
            if (sender.args.length == 3) return Collections.singleton("[<amount>]");
            // x, world, selector, player
            if (sender.args.length == 4) {
                final List<String> results = new ArrayList<>();
                results.add(StringUtility.formatNumber(location.getX(), "#.##"));
                for (final World world : Bukkit.getWorlds()) results.add("#" + world.getName());
                plugin.selectorManager.addKeysTo(results, Player.class);
                results.addAll(BukkitUtility.getOnlinePlayerNames());
                return results;
            }
            final String argument3 = sender.getArgument(3);
            final int argOffset = argument3 != null && Bukkit.getWorld(argument3.substring(1)) != null ? 1 : 0;
            // x
            if (sender.args.length == 5 && argOffset == 1) return Collections.singleton(StringUtility.formatNumber(location.getX(), "#.##"));
            // y
            if (sender.args.length == 5 + argOffset) return Collections.singleton(StringUtility.formatNumber(location.getY(), "#.##"));
            // z
            if (sender.args.length == 6 + argOffset) return Collections.singleton(StringUtility.formatNumber(location.getZ(), "#.##"));
            // yaw
            if (sender.args.length == 7 + argOffset) return Collections.singleton(StringUtility.formatNumber(location.getYaw(), "#.##"));
            // pitch
            if (sender.args.length == 8 + argOffset) return Collections.singleton(StringUtility.formatNumber(location.getPitch(), "#.##"));
        }

        return null;
    }

    @Nullable
    private List<Location> getLocations(@NotNull AnnoyingSender sender) {
        final String argument3 = sender.getArgument(3);
        if (argument3 == null) return null;

        // selector
        final SelectorOptional<Player> selector = SelectorOptional.of(sender, argument3, Player.class);
        if (selector.isPresent()) {
            // Expand selector
            final List<Player> players = selector.getSelector().expand(sender);
            if (players == null) {
                sender.invalidArgument(3);
                return null;
            }

            // Return selector player locations
            final List<Location> locations = new ArrayList<>();
            for (final Player player : players) locations.add(player.getLocation());
            return locations;
        }

        // Not enough arguments
        if (sender.args.length < 6) {
            sender.invalidArguments();
            return null;
        }

        // world
        final World world;
        int argOffset = 0;
        if (argument3.startsWith("#")) {
            // Check if enough arguments
            if (sender.args.length < 7) {
                sender.invalidArguments();
                return null;
            }

            // Get world
            world = Bukkit.getWorld(argument3.substring(1));
            if (world == null) {
                sender.invalidArgument(3);
                return null;
            }
            argOffset = 1;
        } else {
            if (!sender.checkPlayer()) return null;
            world = sender.getPlayer().getWorld();
        }

        // x, y, z
        final Double x = sender
                .getArgumentOptionalFlat(3 + argOffset, Mapper::toDouble)
                .orElse(null);
        if (x == null) return null;
        final Double y = sender
                .getArgumentOptionalFlat(4 + argOffset, Mapper::toDouble)
                .orElse(null);
        if (y == null) return null;
        final Double z = sender
                .getArgumentOptionalFlat(5 + argOffset, Mapper::toDouble)
                .orElse(null);
        if (z == null) return null;
        final Location location = new Location(world, x, y, z);

        // yaw
        if (sender.args.length >= 7 + argOffset) {
            final Float yaw = sender
                    .getArgumentOptionalFlat(6 + argOffset, Mapper::toFloat)
                    .orElse(null);
            if (yaw == null) return null;
            location.setYaw(yaw);
        }

        // pitch
        if (sender.args.length >= 8 + argOffset) {
            final Float pitch = sender
                    .getArgumentOptionalFlat(7 + argOffset, Mapper::toFloat)
                    .orElse(null);
            if (pitch == null) return null;
            location.setPitch(pitch);
        }

        return Collections.singletonList(location);
    }
}
