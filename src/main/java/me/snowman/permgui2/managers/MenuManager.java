package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class MenuManager {
    private final PermGUI permGUI;
    private final MessageManager messageManager;
    private final ItemManager itemManager;
    private final PermsManager permsManager;

    private final Map<UUID, Menu> menus = new HashMap<>();

    public MenuManager(PermGUI permGUI, ItemManager itemManager, MessageManager messageManager, PermsManager permsManager) {
        this.permGUI = permGUI;
        this.messageManager = messageManager;
        this.itemManager = itemManager;
        this.permsManager = permsManager;
    }

    public Map<UUID, Menu> getMenus() {
        return menus;
    }

    public void open(Player player, Menu menu) {
        player.openInventory(menu.getInventory());
        menus.put(player.getUniqueId(), menu);
    }

    public Menu getMenu(Player player) {
        return getMenus().get(player.getUniqueId());
    }

    public boolean hasMenu(Player player) {
        return getMenus().containsKey(player.getUniqueId());
    }

    public Menu getMenu(String fileName, String itemName) {
        FileConfiguration menu = getMenuFile(fileName);
        String strippedItemName = ChatColor.stripColor(itemName);
        String finalStrippedItemName = strippedItemName;
        if (strippedItemName == null
                || Bukkit.getPlayer(strippedItemName) == null
                || Arrays.asList(permsManager.getPerms().getGroups()).contains(strippedItemName)
                || Arrays.stream(Bukkit.getPluginManager().getPlugins()).anyMatch(plugin -> plugin.getName().equalsIgnoreCase(finalStrippedItemName)))
            strippedItemName = "";
        return new Menu()
                .setTarget(strippedItemName)
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
        return getMenu(menu, null);
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
