package me.snowman.permgui2.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;

public class Menu {

    private String title;
    private int size;
    private List<String> commands;
    private InventoryType inventoryType = InventoryType.CHEST;
    private String listType;
    private List<MenuItem> items;
    private Inventory inventory;
    private String target;
    private int page = 1;

    public Menu() {
    }

    public Menu setTitle(String title) {
        if (getTarget() != null) title = title.replace("%target%", getTarget());
        this.title = title;
        return this;
    }

    public Menu setSize(int size){
        this.size = size;
        return this;
    }

    public Menu setCommands(List<String> commands){
        this.commands = commands;
        return this;
    }

    public Menu setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
        return this;
    }

    public Menu setListType(String listType) {
        this.listType = listType;
        return this;
    }

    public Menu build() {
        this.inventory = Bukkit.createInventory(null, getSize(), getTitle());
        if (getListType() != null) {
            LinkedList<String> targets = retrieveTargets(getListType());

            int page = getPage();
            int size = targets.size();
            int maxPage = size / 45 + 1;
            if (size % 45 == 0) maxPage = size / 45;
            if (page > maxPage) page = maxPage;

            for (int slot = 0; slot < 45; slot++) {
                int index = slot + ((page - 1) * 45);
                if (index < size) {
                    String target = targets.get(index);
                    for (MenuItem item : getItems()) {
                        if (item.getSlot() == 0) {
                            String name = item.getName();
                            item.setName(item.getName().replace("%target%", target));
                            getInventory().setItem(slot, item.getItem());
                            item.setName(name);
                        }
                    }
                } else break;
            }
            for (MenuItem item : getItems()) {
                if (item.getSlot() != 0) {
                    if (size > 45 && page == 1) {
                        if (item.getActions().contains("[NEXTPAGE]"))
                            getInventory().setItem(item.getSlot(), item.getItem());
                    } else if (size > 45) {
                        getInventory().setItem(item.getSlot(), item.getItem());
                    } else if (page == maxPage && size > 90) {
                        if (item.getActions().contains("[PREVIOUSPAGE]"))
                            getInventory().setItem(item.getSlot(), item.getItem());
                    }
                }
            }
        } else {
            for (MenuItem item : getItems()) {
                getInventory().setItem(item.getSlot(), item.getItem());
            }
        }
        return this;
    }

    public String getTarget() {
        return target;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public Menu setItems(List<MenuItem> items) {
        this.items = items;
        return this;
    }

    public int getSize() {
        return size;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public List<String> getCommands() {
        return commands;
    }

    public String getListType() {
        return listType;
    }

    public String getTitle() {
        return title;
    }

    public Menu setTarget(String target) {
        if (this.target == null) this.target = target;
        return this;
    }

    public Menu setItems(MenuItem... items) {
        this.items = Arrays.asList(items);
        return this;
    }

    public LinkedList<String> retrieveTargets(String listType) {
        switch (listType) {
            case "players":
                return new LinkedList<>(getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
            case "plugins":
                return new LinkedList<>(Arrays.stream(getServer().getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toList()));
            case "perms":
                //return new LinkedList<>(getServer().getPluginManager().getPlugin(ChatColor.stripColor()).getDescription().getPermissions().stream().map(Permission::getName).collect(Collectors.toList()));
        }
        return null;
    }

    public int getPage() {
        return page;
    }

    public void addPage() {
        page += 1;
    }

    public void substractPage() {
        page -= 1;
    }
}
