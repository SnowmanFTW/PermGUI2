package me.snowman.permgui2.api;

import me.snowman.permgui2.PermGUI;
import me.snowman.permgui2.managers.*;

public class PermGUIAPI {
    private final PermGUI permGUI = PermGUI.getPlugin(PermGUI.class);
    private final UserManager userManager = new UserManager();
    private final FileManager fileManager = new FileManager(permGUI);
    private final PermsManager permsManager = new PermsManager(permGUI);
    private final MessageManager messageManager = new MessageManager(permGUI, fileManager);
    private final ItemManager itemManager = new ItemManager(permGUI, messageManager);
    private final PremadeManager premadeManager = new PremadeManager(permGUI, fileManager, permsManager, messageManager);
    private final MenuManager menuManager = new MenuManager(permGUI, itemManager, messageManager, permsManager, premadeManager);


    public PermsManager getPermsManager() {
        return permsManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public PremadeManager getPremadeManager() {
        return premadeManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
