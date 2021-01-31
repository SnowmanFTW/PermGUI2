package me.snowman.permgui2.commands;

import me.snowman.permgui2.managers.MenuManager;
import me.snowman.permgui2.managers.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Perms implements CommandExecutor {
    private final MenuManager menuManager;
    private final UserManager userManager;

    public Perms(MenuManager menuManager, UserManager userManager) {
        this.menuManager = menuManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        menuManager.open(userManager.getUser(player), menuManager.getMainMenu());
        return true;
    }
}
