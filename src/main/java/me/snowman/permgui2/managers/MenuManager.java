package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import me.snowman.permgui2.objects.Menu;
import me.snowman.permgui2.objects.User;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;

import java.io.File;

import static org.bukkit.Bukkit.getServer;

public class MenuManager {
    private final PermGUI permGUI;
    private final MessageManager messageManager;
    private final ItemManager itemManager;
    private final PermsManager permsManager;
    private final UserManager userManager;

    public MenuManager(PermGUI permGUI, ItemManager itemManager, MessageManager messageManager, PermsManager permsManager, UserManager userManager) {
        this.permGUI = permGUI;
        this.messageManager = messageManager;
        this.itemManager = itemManager;
        this.permsManager = permsManager;
        this.userManager = userManager;
    }

    public void open(User user, Menu menu) {
        menu.setTitle(menu.getTitle().replace("%target%", user.getTarget()).replace("%plugin%", user.getPlugin()));
        if (menu.getListType() == null) menu.build();
        else menu.buildList(user, permsManager);
        user.getPlayer().openInventory(menu.getInventory());
        user.setMenu(menu);
    }

    public Menu getMenu(User user) {
        return user.getMenu();
    }

    public boolean hasMenu(User user) {
        return user.getMenu() != null;
    }

    public Menu getMenu(String fileName) {
        FileConfiguration menu = getMenuFile(fileName);
        return new Menu()
                .setItems(itemManager.getItems(fileName))
                .setTitle(messageManager.color(menu.getString("title")))
                .setSize(menu.getInt("size"))
                .setCommands(menu.getStringList("open-commands"))
                .setInventoryType(InventoryType.valueOf(menu.getString("inventory-type")))
                .setListType(menu.getString("list"))
                .build();
    }

    public Menu getMainMenu() {
        String menu = permGUI.getConfig().getString("MainMenu");
        return getMenu(menu);
    }

    public FileConfiguration getMenuFile(String fileName) {
        File menuFile = new File(permGUI.getDataFolder(), "menus" + File.separator + fileName);
        if (!menuFile.exists()) {
            getServer().getConsoleSender().sendMessage(fileName + " doesn't exist!");
            return null;
        }
        return YamlConfiguration.loadConfiguration(menuFile);
    }
}
