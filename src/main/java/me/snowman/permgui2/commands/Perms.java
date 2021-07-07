package me.snowman.permgui2.commands;

import me.snowman.permgui2.managers.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Perms implements CommandExecutor, TabCompleter {
    private final MenuManager menuManager;
    private final UserManager userManager;
    private final MessageManager messageManager;
    private final FileManager fileManager;
    private final BotManager botManager;

    public Perms(MenuManager menuManager, UserManager userManager, MessageManager messageManager, FileManager fileManager, BotManager botManager) {
        this.menuManager = menuManager;
        this.userManager = userManager;
        this.messageManager = messageManager;
        this.fileManager = fileManager;
        this.botManager = botManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messageManager.getMessages("NoConsole"));
            return true;
        }
        Player player = (Player) sender;
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
        if(!player.hasPermission("permgui.link")){
            player.sendMessage(messageManager.getMessages("NoPerm"));
            return true;
        }
        if(arg.equalsIgnoreCase("link")){
            String code = UUID.randomUUID().toString().substring(0, 10).replace("-", "");
            botManager.getLinkCodes().put(player.getUniqueId(), code);
            player.sendMessage(messageManager.getMessages("DMBot").replace("%code%", code));
        }
        if (!player.hasPermission("permgui.use") || !player.hasPermission("permgui2.use")){
            player.sendMessage(messageManager.getMessages("NoPerm"));
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tabComplete = new ArrayList<>();
        if(command.getName().equalsIgnoreCase("perms2")){
            if(args.length == 0){
            }
            if(args.length == 1){
                if(args[0].contains("r")){
                    tabComplete.add("reload");
                }else if(args[0].contains("h")) {
                    tabComplete.add("help");
                }else if(args[0].contains("l")){
                    tabComplete.add("link");
                }else{
                    tabComplete.add("reload");
                    tabComplete.add("help");
                    tabComplete.add("link");
                }
            }
        }
        return tabComplete;
    }
}
