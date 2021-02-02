package me.snowman.permgui2.objects;

import me.snowman.permgui2.managers.PermsManager;
import me.snowman.permgui2.managers.PremadeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.Permission;
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
    private int page = 1;

    public Menu() {
    }

    public Menu setTitle(String title) {
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
        for (MenuItem item : getItems()) {
            getInventory().setItem(item.getSlot(), item.getItem());
        }
        return this;
    }

    public void buildList(User user, PermsManager permsManager, PremadeManager premadeManager) {
        this.inventory = Bukkit.createInventory(null, getSize(), getTitle());
        LinkedList<String> targets = retrieveTargets(getListType(), user, permsManager, premadeManager);

        int page = getPage();
        int size = targets.size();
        int maxPage = size / 45 + 1;
        if (size % 45 == 0) maxPage = size / 45;
        if (page > maxPage) page = maxPage;
        if (size == 0) {
            return;
        }

        for (int slot = 0; slot < 45; slot++) {
            int index = slot + ((page - 1) * 45);
            if (index < size) {
                String target = targets.get(index);
                for (MenuItem item : getItems()) {
                    if (item.getSlot() == 0) {
                        String name = item.getName();
                        item.setName(item.getName().replace("%target%", target));
                        if (getListType().equals("perms")) {
                            if (Bukkit.getServer().getPlayer(user.getTarget()) != null) {
                                Player player = Bukkit.getServer().getPlayer(user.getTarget());
                                if (player.hasPermission(target)) item.setName(ChatColor.GREEN + item.getName());
                                else item.setName(ChatColor.DARK_RED + item.getName());
                            } else if (Arrays.stream(permsManager.getPerms().getGroups()).map(group -> group.equals(user.getTarget())).anyMatch(aBoolean -> true)) {
                                String group = user.getTarget();
                                if (permsManager.getPerms().groupHas((World) null, group, target))
                                    item.setName(ChatColor.GREEN + item.getName());
                                else item.setName(ChatColor.DARK_RED + item.getName());
                            }
                        }
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

    public Menu setItems(MenuItem... items) {
        this.items = Arrays.asList(items);
        return this;
    }

    public LinkedList<String> retrieveTargets(String listType, User user, PermsManager permsManager, PremadeManager premadeManager) {
        switch (listType) {
            case "players":
                return new LinkedList<>(getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
            case "groups":
            case "changegroup":
            case "addgroup":
                return new LinkedList<>(Arrays.asList(permsManager.getPerms().getGroups()));
            case "plugins":
                return new LinkedList<>(Arrays.stream(getServer().getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toList()));
            case "perms":
                return new LinkedList<>(getServer().getPluginManager().getPlugin(user.getPlugin()).getDescription().getPermissions().stream().map(Permission::getName).collect(Collectors.toList()));
            case "premades":
                return new LinkedList<>(premadeManager.getPremades());
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
