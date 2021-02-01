package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class MessageManager {
    private final PermGUI main;
    private final FileManager fileManager;

    public MessageManager(PermGUI main, FileManager fileManager){
        this.main = main;
        this.fileManager = fileManager;
    }


    public String getPrefix(){
        return getMessages("Prefix") + " ";
    }

    public String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public List<String> color(List<String> list){
        return list.stream().map(this::color).collect(Collectors.toList());
    }

    public String getMessages(String s){
        if (s.equals("Prefix")) return fileManager.getMessages().getString("Prefix");
        return ChatColor.translateAlternateColorCodes('&', fileManager.getMessages().getString("Prefix") + fileManager.getMessages().getString(s));
    }
}
