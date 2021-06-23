package me.snowman.permgui2;

import me.snowman.permgui2.Bot.Listeners;
import me.snowman.permgui2.api.PermGUIAPI;
import me.snowman.permgui2.bstats.Metrics;
import me.snowman.permgui2.commands.Perms;
import me.snowman.permgui2.events.ChatListeners;
import me.snowman.permgui2.events.GUIListeners;
import me.snowman.permgui2.events.JoinListener;
import me.snowman.permgui2.managers.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class PermGUI extends JavaPlugin {
    final FileManager fileManager = new FileManager(this);
    final PermsManager permsManager = new PermsManager(this);
    final MessageManager messageManager = new MessageManager(this, fileManager);
    final UserManager userManager = new UserManager();
    final PremadeManager premadeManager = new PremadeManager(this, fileManager, permsManager, messageManager);
    final ItemManager itemManager = new ItemManager(this, messageManager);
    final MenuManager menuManager = new MenuManager(this, itemManager, messageManager, permsManager, premadeManager);
    final BotManager botManager = new BotManager(this, fileManager);
    @Override
    public void onEnable() {
        getCommand("perms2").setExecutor(new Perms(menuManager, userManager, messageManager, fileManager, botManager));
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &bLoaded all commands."));
        getServer().getPluginManager().registerEvents(new GUIListeners(menuManager, itemManager, permsManager, userManager, premadeManager, messageManager), this);
        getServer().getPluginManager().registerEvents(new ChatListeners(messageManager, permsManager, userManager, premadeManager, botManager), this);
        getServer().getPluginManager().registerEvents(new JoinListener(fileManager), this);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &bLoaded all listeners."));

        permsManager.setupChat();
        if(!permsManager.setupPermissions()) return;
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &bLoaded Vault."));
        fileManager.setupConfig();
        fileManager.setupMessages();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &bLoaded all configs."));
        fileManager.setupGUIs();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &bLoaded all menus."));
        botManager.setToken(getConfig().getString("Discord.Bot-Token"));
        botManager.startBot();
        int id = 5646;
        Metrics metrics = new Metrics(this, id);
        addCharts(metrics, permsManager);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &bLoaded metrics."));
        updatePlugin();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &bPlugin loaded. v&f" + getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        botManager.stopBot();
    }

    public void addCharts(Metrics metrics, PermsManager permsManager){
        metrics.addCustomChart(new Metrics.SimplePie("permissionPlugin", () -> permsManager.getPerms().getName()));
    }

    public static PermGUIAPI getAPI(){
        return new PermGUIAPI();
    }

    public void updatePlugin() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String spigotId = "70350";
                    // Open connection with spigot's web API
                    HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + spigotId).openConnection();

                    // Versions
                    String latest = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
                    String installed = getDescription().getVersion();

                    // Check if it's outdated or not
                    if (!installed.equals(latest)) {
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &bYour plugin version (&f" + installed + "&b) is not the latest one (&f" + latest + "&b)"));
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &bYou can download it here: &fhttps://www.spigotmc.org/resources/70350/"));
                    } else {
                        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &bPlugin version is up-to-date (&f" + installed + "&b)."));
                    }
                } catch (Exception e) {
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &fThere was an error connecting to SpigotMC API."));
                }
            }
        }.runTaskLaterAsynchronously(this, 40);
    }


}
