package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
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
        if(fileManager.getMessages().getString("Prefix") == null) return ChatColor.translateAlternateColorCodes('&', "&bPermGUI &f- &fMessages file not found. Restart the server to recreate it.");
        return ChatColor.translateAlternateColorCodes('&', fileManager.getMessages().getString(s));
    }
}
