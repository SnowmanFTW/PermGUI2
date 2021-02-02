package me.snowman.permgui2.commands;

import me.snowman.permgui2.managers.MenuManager;
import me.snowman.permgui2.managers.MessageManager;
import me.snowman.permgui2.managers.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Perms implements CommandExecutor {
    private final MenuManager menuManager;
    private final UserManager userManager;
    private final MessageManager messageManager;

    public Perms(MenuManager menuManager, UserManager userManager, MessageManager messageManager) {
        this.menuManager = menuManager;
        this.userManager = userManager;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messageManager.getMessages("NoConsole"));
            return true;
        }
        Player player = (Player) sender;
        if (player.hasPermission("permgui.use") || player.hasPermission("permgui2.use"))
            menuManager.open(userManager.getUser(player), menuManager.getMainMenu());
        else player.sendMessage(messageManager.getMessages("NoPerm"));
        return true;
    }
}
