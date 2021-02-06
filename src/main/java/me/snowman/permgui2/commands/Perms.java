package me.snowman.permgui2.commands;

import me.snowman.permgui2.managers.FileManager;
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
    private final FileManager fileManager;

    public Perms(MenuManager menuManager, UserManager userManager, MessageManager messageManager, FileManager fileManager) {
        this.menuManager = menuManager;
        this.userManager = userManager;
        this.messageManager = messageManager;
        this.fileManager = fileManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messageManager.getMessages("NoConsole"));
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("permgui.use") || !player.hasPermission("permgui2.use")){
            player.sendMessage(messageManager.getMessages("NoPerm"));
            return true;
        }
        if(args.length == 0) {
            menuManager.open(userManager.getUser(player), menuManager.getMainMenu());
            return true;
        }
        String arg = args[0];
        if(arg.equalsIgnoreCase("help")){
            for(String help: messageManager.getHelp()){
                player.sendMessage(messageManager.color(help));
            }
            return true;
        }
        switch (arg){
            case "help":
                for(String help: messageManager.getHelp()){
                    player.sendMessage(messageManager.color(help));
                }
                break;
            case "reload":
                fileManager.reloadConfig();
                fileManager.reloadMessages();
                player.sendMessage(messageManager.getMessages("Reload"));
                return true;
            case "convert":
                if(fileManager.convertOld(player)) player.sendMessage(messageManager.color(messageManager.getPrefix() + "&bFinished converting messages"));
                return true;
        }
        return true;
    }
}
