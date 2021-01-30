package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private File messagesFile;
    private FileConfiguration messagesCfg;
    private final String[] menus = {"portal", "players", "player_portal", "meta", "plugins", "permissions"};

    private final PermGUI permGUI;
    public FileManager(PermGUI permGUI){
        this.permGUI = permGUI;
    }

    public void setupMessages(){
        if(!permGUI.getDataFolder().exists()){
            permGUI.getDataFolder().mkdir();
        }

        messagesFile = new File(permGUI.getDataFolder(), "messages.yml");
        if(!messagesFile.exists()){
            permGUI.saveResource("messages.yml", true);
            messagesCfg = YamlConfiguration.loadConfiguration(messagesFile);
//            Bukkit.getServer().getConsoleSender().sendMessage(PermGUI.messagesManager.getPrefix() + PermGUI.messagesManager.color("&bMessages file created successfully."));
        }
        if(messagesCfg == null) {
            messagesCfg = YamlConfiguration.loadConfiguration(messagesFile);
        }
    }

    public FileConfiguration getMessages(){
        return messagesCfg;
    }

    public void saveMessages(){
        try{
            messagesCfg.save(messagesFile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void reloadMessages(){
        messagesFile = new File(permGUI.getDataFolder(), "messages.yml");
        messagesCfg = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void setupConfig(){
        if(!permGUI.getDataFolder().exists()){
            permGUI.getDataFolder().mkdir();
        }

        File configFile = new File(permGUI.getDataFolder(), "config.yml");
        if(!configFile.exists()){
            permGUI.saveDefaultConfig();
            if(Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.8")){
                permGUI.getConfig().set("Sound", "BLOCK_NOTE_PLING");
            }
//            Bukkit.getServer().getConsoleSender().sendMessage(PermGUI.messagesManager.getPrefix() + PermGUI.messagesManager.color("&bConfig file created successfully."));
        }
    }

    public FileConfiguration getConfig(){
        return permGUI.getConfig();
    }

    public void reloadConfig(){
        permGUI.reloadConfig();
    }

    public void saveConfig(){ permGUI.saveConfig(); }

    public void setupGUIs(){
        File menuFolder = new File(permGUI.getDataFolder(),  "menus" + File.separator);
        if(!menuFolder.exists()){
            menuFolder.mkdir();
        }
        for(String menu: menus){
            File menuFile = new File(menuFolder, menu + ".yml");
            if(!menuFile.exists()){
                permGUI.saveResource("menus" + File.separator + menu + ".yml", true);
//            Bukkit.getServer().getConsoleSender().sendMessage(PermGUI.messagesManager.getPrefix() + PermGUI.messagesManager.color("&bMessages file created successfully."));
            }
        }
    }
}
