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
        return fileManager.getMessages().getString("Prefix") + " ";
    }

    public String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public List<String> color(List<String> list) {
        return list.stream().map(this::color).collect(Collectors.toList());
    }

    public String getMessages(String s) {
        return ChatColor.translateAlternateColorCodes('&', getPrefix() + fileManager.getMessages().getString(s));
    }

    public String getPercent(float number, float max) {
        float percent = (max / number) * 100;
        if (percent % 1 == 0.0) return color("&b" + (int) percent + "&f%");
        return color("&b" + percent + "&f%");
    }

}
