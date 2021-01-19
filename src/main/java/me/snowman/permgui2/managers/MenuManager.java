package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class MenuManager {
    private final PermGUI permGUI;
    private final MessageManager messageManager;

    private Map<UUID, Menu> menus = new HashMap<>();

    public MenuManager(PermGUI permGUI, MessageManager messageManager){
        this.permGUI = permGUI;
        this.messageManager = messageManager;
    }

    public Map<UUID, Menu> getMenus() {
        return menus;
    }

    public void setMenu(Player player, Menu menu){
        menus.put(player.getUniqueId(), menu);
    }

    public Menu getMenu(Player player){
        return getMenus().get(player.getUniqueId());
    }

    public Menu getMenu(String fileName){
        File menuFile = new File(permGUI.getDataFolder(), "menus" + File.separator + fileName + ".yml");
        if(!menuFile.exists()){
            getServer().getConsoleSender().sendMessage(fileName + ".yml doesn't exist!");
            return null;
        }
        FileConfiguration menu = YamlConfiguration.loadConfiguration(menuFile);
        return new Menu()
                .setTitle(messageManager.color(menu.getString("title")))
                .setSize(menu.getInt("size"))
                .setCommands(menu.getStringList("open-commands"))
                .setInventoryType(InventoryType.valueOf(menu.getString("inventory-type")))
                .setListType(menu.getString("list"))
                .build();
    }
}
