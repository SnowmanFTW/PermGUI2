package me.snowman.permgui2.events;

import me.snowman.permgui2.managers.FileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final FileManager fileManager;
    public JoinListener(FileManager fileManager){
        this.fileManager = fileManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        fileManager.setupPlayer(event.getPlayer());
    }
}
