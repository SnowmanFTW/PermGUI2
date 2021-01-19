package me.snowman.permgui2.commands;

import me.snowman.permgui2.managers.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Perms implements CommandExecutor {
    private final MenuManager menuManager;

    public Perms(MenuManager menuManager){
        this.menuManager = menuManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        menuManager.open(player, menuManager.getMainMenu());
        return true;
    }
}
