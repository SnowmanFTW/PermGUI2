package me.snowman.permgui2.events;

import me.snowman.permgui2.managers.*;
import me.snowman.permgui2.objects.Menu;
import me.snowman.permgui2.objects.MenuItem;
import me.snowman.permgui2.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GUIListeners implements Listener {
    private final MenuManager menuManager;
    private final ItemManager itemManager;
    private final PermsManager permsManager;
    private final UserManager userManager;
    private final MessageManager messageManager;

    public GUIListeners(MenuManager menuManager, ItemManager itemManager, PermsManager permsManager, UserManager userManager, MessageManager messageManager) {
        this.menuManager = menuManager;
        this.permsManager = permsManager;
        this.itemManager = itemManager;
        this.userManager = userManager;
        this.messageManager = messageManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        User user = userManager.getUser(player);
        if (!menuManager.hasMenu(user)) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
        event.setCancelled(true);
        Menu menu = menuManager.getMenu(user);
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem.getItemMeta() == null) return;
        MenuItem item = itemManager.getItem(menu, currentItem, currentItem.getItemMeta().getDisplayName());
        if (item == null) return;
        String itemName = ChatColor.stripColor(item.getName());

        if (menu.getListType() != null) {
            switch (menu.getListType()) {
                case "players":
                case "groups":
                    user.setTarget(itemName);
                    break;
                case "plugins":
                    user.setPlugin(itemName);
                    break;
            }
        }
        for (String actions : item.getActions()) {
            String action = actions;
            if (actions.contains(" ")) action = actions.substring(0, actions.indexOf(" "));
            String arguments = actions.substring(actions.indexOf(" ") + 1);
            String targetString = arguments.replace("%target%", user.getTarget());
            Player target = Bukkit.getServer().getPlayer(targetString);
            switch (action) {
                case "[OPEN]":
                    Menu openedMenu = menuManager.getMenu(arguments);
                    user.setForceClosed(false);
                    menuManager.open(user, openedMenu);
                    break;
                case "[CHANGEPERM]":
                    if (Bukkit.getServer().getPlayer(targetString) != null) {
                        if (target == null) return;
                        if (target.hasPermission(itemName))
                            permsManager.getPerms().playerRemove(null, target, itemName);
                        else permsManager.getPerms().playerAdd(null, target, itemName);
                        break;
                    } else if (Arrays.stream(permsManager.getPerms().getGroups()).map(group -> group.equals(user.getTarget())).anyMatch(aBoolean -> true)) {
                        if (permsManager.getPerms().groupHas((World) null, targetString, itemName))
                            permsManager.getPerms().groupRemove((World) null, targetString, itemName);
                        else permsManager.getPerms().groupAdd((World) null, targetString, itemName);
                        break;
                    }
                    break;
                case "[CHANGEGROUP]":
                    for (String group : permsManager.getPerms().getPlayerGroups(target)) {
                        permsManager.getPerms().playerRemoveGroup(null, target, group);
                    }
                    permsManager.getPerms().playerAddGroup(null, target, itemName);
                    user.getPlayer().closeInventory();
                    break;
                case "[ADDGROUP]":
                    permsManager.getPerms().playerAddGroup(null, target, itemName);
                    user.getPlayer().closeInventory();
                    break;
                case "[PREFIX]":
                case "[SUFFIX]":
                    user.setChat(action);
                    if (action.toLowerCase().contains("prefix"))
                        player.sendMessage(messageManager.getMessages("PrefixChat"));
                    else player.sendMessage(messageManager.getMessages("SuffixChat"));
                    break;
                case "[CLOSE]":
                    user.setForceClosed(true);
                    user.getPlayer().closeInventory();
                    user.setMenu(null);
            }
        }
    }

    @EventHandler
    public void closeMenu(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        User user = userManager.getUser(player);

        if (!user.hasForceClosed()) {
            user.setMenu(null);
        }
    }
}
