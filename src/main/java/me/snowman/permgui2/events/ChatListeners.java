package me.snowman.permgui2.events;

import me.snowman.permgui2.managers.MenuManager;
import me.snowman.permgui2.managers.PermsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListeners implements Listener {
    private final MenuManager menuManager;
    private final PermsManager permsManager;

    public ChatListeners(MenuManager menuManager, PermsManager permsManager){
        this.menuManager = menuManager;
        this.permsManager = permsManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
    }
}
