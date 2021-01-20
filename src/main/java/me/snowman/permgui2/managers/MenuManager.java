package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.io.File;
import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class MenuManager {
    private final PermGUI permGUI;
    private final MessageManager messageManager;

    private final Map<UUID, Menu> menus = new HashMap<>();

    public MenuManager(PermGUI permGUI, MessageManager messageManager) {
        this.permGUI = permGUI;
        this.messageManager = messageManager;
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

    public Menu getMenu(String fileName) {
        File menuFile = new File(permGUI.getDataFolder(), "menus" + File.separator + fileName + ".yml");
        if (!menuFile.exists()) {
            getServer().getConsoleSender().sendMessage(fileName + ".yml doesn't exist!");
            return null;
        }
        FileConfiguration menu = YamlConfiguration.loadConfiguration(menuFile);
        ConfigurationSection itemsSection = menu.getConfigurationSection("items");
        List<MenuItem> items = new ArrayList<>();
        itemsSection.getKeys(false)
                .forEach(item -> items.add(new MenuItem(Material.matchMaterial(itemsSection.getString(item + ".material")), itemsSection.getInt(item + ".amount"))
                        .setName(messageManager.color(itemsSection.getString(item + ".name")))
                        .setLore(messageManager.color(itemsSection.getStringList(item + ".lore")))
                        .setActions(itemsSection.getStringList(item + ".actions"))
                        .setSlot(itemsSection.getInt(item + ".slot"))
                        .build()));
        return new Menu()
                .setItems(items)
                .setTitle(messageManager.color(menu.getString("title")))
                .setSize(menu.getInt("size"))
                .setCommands(menu.getStringList("open-commands"))
                .setInventoryType(InventoryType.valueOf(menu.getString("inventory-type")))
                .setListType(menu.getString("list"))
                .build();
    }

    public Menu getMainMenu(){
        String menu = permGUI.getConfig().getString("MainMenu").replace(".yml", "");
        return getMenu(menu);
    }
}
