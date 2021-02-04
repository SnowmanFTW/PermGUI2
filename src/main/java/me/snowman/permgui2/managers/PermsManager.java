package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PermsManager {
    private final PermGUI permGUI;
    private static Permission perms = null;
    private static Chat chat = null;
    public PermsManager(PermGUI permGUI){
        this.permGUI = permGUI;
    }

    public boolean setupPermissions() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getServer().getConsoleSender().sendMessage("&cVault &4not found. Install &cVault &4then try again.");
            Bukkit.getServer().getPluginManager().disablePlugin(permGUI);
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if(rsp == null){
            Bukkit.getServer().getConsoleSender().sendMessage("&cPermission plugin &4not found. Install a &cpermission plugin &4then try again.");
            Bukkit.getServer().getPluginManager().disablePlugin(permGUI);
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }

    public boolean setupChat() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getServer().getConsoleSender().sendMessage("&cVault &4not found. Install &cVault &4then try again.");
            Bukkit.getServer().getPluginManager().disablePlugin(permGUI);
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if(rsp == null){
            Bukkit.getServer().getConsoleSender().sendMessage("&cChat plugin &4not found. Install a &cchat plugin &4then try again.");
            Bukkit.getServer().getPluginManager().disablePlugin(permGUI);
            return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }

    public Permission getPerms() {
        return perms;
    }

    public Chat getChat() {
        return chat;
    }

    public boolean isLuckPerms() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("LuckPerms");
    }

    public LuckPerms getLuckPerms() {
        return LuckPermsProvider.get();
    }
}
