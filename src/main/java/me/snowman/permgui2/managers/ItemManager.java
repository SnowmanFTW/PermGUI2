package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import me.snowman.permgui2.objects.Menu;
import me.snowman.permgui2.objects.MenuItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
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
                .forEach(item -> {
                    MenuItem menuItem = new MenuItem(Material.matchMaterial(itemsSection.getString(item + ".material")), itemsSection.getInt(item + ".amount"))
                            .setName(messageManager.color(itemsSection.getString(item + ".name")))
                            .setLore(messageManager.color(itemsSection.getStringList(item + ".lore")))
                            .setActions(itemsSection.getStringList(item + ".actions"))
                            .setSlot(itemsSection.getInt(item + ".slot"))
                            .setSkullOwner(itemsSection.getString(item + ".skin"))
                            .build();
                    if(!itemsSection.isSet(item + ".slot")){
                        menuItem.setSlot(-1);
                    }
                    items.add(menuItem);
                });
        return items;
    }

    public MenuItem getItem(Menu menu, ItemStack itemStack, String target) {
        if(itemStack.getItemMeta() == null) return null;
        for (MenuItem item : menu.getItems()) {
            if(item.getItem().getItemMeta() == null) continue;
            item.setName(item.getName().replace("%target%", target));
            if(item.getItem().getType().equals(Material.PLAYER_HEAD)){
                List<String> itemLore = itemStack.getItemMeta().getLore();
                if (itemStack.getItemMeta().getLore() == null) itemLore = new ArrayList<>();
                if(itemLore == null) itemLore = new ArrayList<>();
                if(!item.getItem().getItemMeta().hasLore() && itemLore.isEmpty()) return item;
                if(item.getSlot() == -1) continue;
                if(item.getName().equalsIgnoreCase(itemStack.getItemMeta().getDisplayName()) && item.getLore().equals(itemLore)) return item;
            }
            if(isSimilar(item.getItem(), itemStack)) return item;
        }
        return null;
    }

    public boolean isSimilar(ItemStack item1, ItemStack item2){
        if(item1.getItemMeta() == null && item2.getItemMeta() == null && item2.getType().equals(item1.getType())) return true;
        if(item1.getItemMeta() == null) return false;
        if(item2.getItemMeta() == null) return false;
        if(item2.getItemMeta().getLore() == null && item1.getItemMeta().getLore() == null && item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName())) return true;
        return item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName()) &&
                item1.getItemMeta().getLore().equals(item2.getItemMeta().getLore());
    }

}
