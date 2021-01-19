package me.snowman.permgui2;

import me.snowman.permgui2.commands.Perms;
import me.snowman.permgui2.events.ChatListeners;
import me.snowman.permgui2.events.GUIListeners;
import me.snowman.permgui2.managers.*;
import me.snowman.permgui2.permissions.PermissionModify;
import org.bukkit.plugin.java.JavaPlugin;

public class PermGUI extends JavaPlugin {

    @Override
    public void onEnable() {
        final FileManager fileManager = new FileManager(this);
        final MessageManager messageManager = new MessageManager(this, fileManager);
        final MenuManager menuManager = new MenuManager(this, messageManager);
        final PermsManager permsManager = new PermsManager(this);
        final PermissionModify permissionModify = new PermissionModify(permsManager);
        getCommand("perms2").setExecutor(new Perms(menuManager));
        getServer().getPluginManager().registerEvents(new GUIListeners(menuManager, permsManager), this);
        getServer().getPluginManager().registerEvents(new ChatListeners(menuManager, permsManager), this);

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
