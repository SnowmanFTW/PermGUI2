package me.snowman.permgui2;

import me.snowman.permgui2.api.PermGUIAPI;
import me.snowman.permgui2.bstats.Metrics;
import me.snowman.permgui2.commands.Perms;
import me.snowman.permgui2.events.ChatListeners;
import me.snowman.permgui2.events.GUIListeners;
import me.snowman.permgui2.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Callable;

public class PermGUI extends JavaPlugin {
    private final int id = 5646;
    public PermsManager permsManager;

    @Override
    public void onEnable() {
        final FileManager fileManager = new FileManager(this);
        permsManager = new PermsManager(this);
        final MessageManager messageManager = new MessageManager(this, fileManager);
        final UserManager userManager = new UserManager();
        final PremadeManager premadeManager = new PremadeManager(this, fileManager, permsManager, messageManager);
        final ItemManager itemManager = new ItemManager(this, messageManager);
        final MenuManager menuManager = new MenuManager(this, itemManager, messageManager, permsManager, premadeManager);
        getCommand("perms2").setExecutor(new Perms(menuManager, userManager, messageManager, fileManager));
        getServer().getPluginManager().registerEvents(new GUIListeners(menuManager, itemManager, permsManager, userManager, premadeManager, messageManager), this);
        getServer().getPluginManager().registerEvents(new ChatListeners(messageManager, permsManager, userManager, premadeManager), this);

        permsManager.setupChat();
        permsManager.setupPermissions();
        fileManager.setupConfig();
        fileManager.setupMessages();
        fileManager.setupGUIs();
        Metrics metrics = new Metrics(this, id);
        addCharts(metrics, permsManager);
    }

    @Override
    public void onDisable() {

    }

    public void addCharts(Metrics metrics, PermsManager permsManager){
        metrics.addCustomChart(new Metrics.SimplePie("permissionPlugin", () -> permsManager.getPerms().getName()));
    }

    public static PermGUIAPI getAPI(){
        return new PermGUIAPI();
    }


}
