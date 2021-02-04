package me.snowman.permgui2.objects;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class User {
    private final Player player;
    private String target = "";
    private String plugin = "";
    private Menu menu;
    private String chat;
    private boolean forceClosed;

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

    public String getChat() {
        return chat;
    }

    public void removeChat() {
        this.chat = null;
    }

    public boolean isChat() {
        return chat != null;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public boolean hasForceClosed() {
        return forceClosed;
    }

    public void setForceClosed(boolean forceClosed) {
        this.forceClosed = forceClosed;
    }

    public void sendActionBar(String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText((message)));
    }
}
