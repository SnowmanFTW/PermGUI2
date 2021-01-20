package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class ItemManager {
    private final PermGUI permGUI;
    private final MessageManager messageManager;

    public ItemManager(PermGUI permGUI, MessageManager messageManager) {
        this.permGUI = permGUI;
        this.messageManager = messageManager;
    }

    public List<MenuItem> getItems(String fileName) {
        File menuFile = new File(permGUI.getDataFolder(), "menus" + File.separator + fileName);
        if (!menuFile.exists()) {
            getServer().getConsoleSender().sendMessage(fileName + " doesn't exist!");
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
        return items;
    }

    public MenuItem getItem(Menu menu, ItemStack itemStack) {
        for (MenuItem item : menu.getItems()) {
            if (item.getItem().isSimilar(itemStack)) return item;
        }
        return null;
    }

}
