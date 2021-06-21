package me.snowman.permgui2.commands;

import me.snowman.permgui2.managers.FileManager;
import me.snowman.permgui2.managers.MenuManager;
import me.snowman.permgui2.managers.MessageManager;
import me.snowman.permgui2.managers.UserManager;
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
import java.util.stream.Collectors;

public class Perms implements CommandExecutor, TabCompleter {
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
            case "test":
                try {
                    HttpsURLConnection connection = (HttpsURLConnection) new URL("https://essinfo.xeya.me/permissions.html").openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    System.out.println(bufferedReader.lines().collect(Collectors.toList()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                }else if(args[0].contains("h")){
                    tabComplete.add("help");
                }else{
                    tabComplete.add("reload");
                    tabComplete.add("help");
                }
            }
        }
        return tabComplete;
    }
}
