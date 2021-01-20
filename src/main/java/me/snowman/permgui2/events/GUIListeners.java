package me.snowman.permgui2.events;

import me.snowman.permgui2.managers.Menu;
import me.snowman.permgui2.managers.MenuItem;
import me.snowman.permgui2.managers.MenuManager;
import me.snowman.permgui2.managers.PermsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListeners implements Listener {
    private final MenuManager menuManager;
    private final PermsManager permsManager;

    public GUIListeners(MenuManager menuManager, PermsManager permsManager) {
        this.menuManager = menuManager;
        this.permsManager = permsManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!menuManager.hasMenu(player)) return;
        MenuItem item = new MenuItem(event.getCurrentItem());

        for (String actions : item.getActions()) {
            String action = actions.substring(0, actions.indexOf(" "));
            String menuName = actions.substring(action.indexOf(" "));
            Menu menu = menuManager.getMenu(menuName);
            switch (action) {
                case "[OPEN]":
                    menuManager.open(player, menu);
                    break;
            }
        }
        event.setCancelled(true);
    }
}
