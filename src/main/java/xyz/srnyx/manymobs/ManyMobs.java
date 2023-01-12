package xyz.srnyx.manymobs;

import org.bukkit.ChatColor;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;


public class ManyMobs extends AnnoyingPlugin {
    public ManyMobs() {
        super();

        // Options
        options.colorLight = ChatColor.GREEN;
        options.colorDark = ChatColor.DARK_GREEN;
        options.commands.add(new ManymobsCommand(this));
    }
}
