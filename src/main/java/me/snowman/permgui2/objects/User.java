package me.snowman.permgui2.objects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class User {
    private final Player player;
    private String target = "";
    private String plugin = "";
    private Menu menu;

    public User(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = ChatColor.stripColor(plugin);
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = ChatColor.stripColor(target);
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
