package me.snowman.permgui2.events;

import me.snowman.permgui2.managers.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListeners implements Listener {
    private final MenuManager menuManager;
    private final ItemManager itemManager;
    private final PermsManager permsManager;

    public GUIListeners(MenuManager menuManager, ItemManager itemManager, PermsManager permsManager) {
        this.menuManager = menuManager;
        this.permsManager = permsManager;
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!menuManager.hasMenu(player)) return;
        Menu menu = menuManager.getMenu(player);
        MenuItem item = itemManager.getItem(menu, event.getCurrentItem());

        for (String actions : item.getActions()) {
            String action = actions.substring(0, actions.indexOf(" "));
            String arguments = actions.substring(actions.indexOf(" ") + 1);
            switch (action) {
                case "[OPEN]":
                    Menu openedMenu = menuManager.getMenu(arguments);
                    menuManager.open(player, openedMenu);
                    break;
            }
        }
        event.setCancelled(true);
    }
}
