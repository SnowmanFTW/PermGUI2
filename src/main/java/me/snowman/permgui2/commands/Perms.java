package me.snowman.permgui2.commands;

import me.snowman.permgui2.PermGUI;
import me.snowman.permgui2.managers.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Perms implements CommandExecutor {
    private final PermGUI plugin;
    private final MenuManager menuManager;

    public Perms(PermGUI plugin, MenuManager menuManager){
        this.plugin = plugin;
        this.menuManager = menuManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String menu = plugin.getConfig().getString("MainMenu").replace(".yml", "");
        menuManager.getMenu(menu).open(player);
        return true;
    }
}
