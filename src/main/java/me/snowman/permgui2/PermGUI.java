package me.snowman.permgui2;

import me.snowman.permgui2.commands.Perms;
import me.snowman.permgui2.events.ChatListeners;
import me.snowman.permgui2.events.GUIListeners;
import me.snowman.permgui2.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class PermGUI extends JavaPlugin {

    @Override
    public void onEnable() {
        final FileManager fileManager = new FileManager(this);
        final PermsManager permsManager = new PermsManager(this);
        final MessageManager messageManager = new MessageManager(this, fileManager);
        final UserManager userManager = new UserManager();
        final PremadeManager premadeManager = new PremadeManager(this, fileManager, permsManager);
        final ItemManager itemManager = new ItemManager(this, messageManager);
        final MenuManager menuManager = new MenuManager(this, itemManager, messageManager, permsManager, premadeManager);
        getCommand("perms2").setExecutor(new Perms(menuManager, userManager, messageManager));
        getServer().getPluginManager().registerEvents(new GUIListeners(menuManager, itemManager, permsManager, userManager, messageManager), this);
        getServer().getPluginManager().registerEvents(new ChatListeners(messageManager, permsManager, userManager, premadeManager), this);

        permsManager.setupChat();
        permsManager.setupPermissions();
        fileManager.setupConfig();
        fileManager.setupMessages();
        fileManager.setupGUIs();
    }

    @Override
    public void onDisable() {

    }
}
