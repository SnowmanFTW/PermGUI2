package me.snowman.permgui2.events;

import me.snowman.permgui2.managers.MenuManager;
import me.snowman.permgui2.managers.PermsManager;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListeners implements Listener {
    private final MenuManager menuManager;
    private final PermsManager permsManager;

    public GUIListeners(MenuManager menuManager, PermsManager permsManager) {
        this.menuManager = menuManager;
        this.permsManager = permsManager;
    }

    public void onClick(InventoryClickEvent event){

    }
}
