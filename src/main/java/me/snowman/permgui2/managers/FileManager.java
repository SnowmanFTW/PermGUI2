package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileManager {

    private File messagesFile;
    private FileConfiguration messagesCfg;

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
        }
    }

    public FileConfiguration getConfig(){
        return permGUI.getConfig();
    }

    public void reloadConfig(){
        permGUI.reloadConfig();
    }

    public void saveConfig(){ permGUI.saveConfig(); }

    public void setupGUIs() {
        File menuFolder = new File(permGUI.getDataFolder(), "menus" + File.separator);
        if (!menuFolder.exists()) {
            menuFolder.mkdir();
        }
        for (String menu : getMenus()) {
            File menuFile = new File(menuFolder, menu.replace("menus/", ""));
            if (!menuFile.exists()) {
                permGUI.saveResource(menu, true);
            }
        }
    }

    public Set<String> getMenus() {
        //Thanks https://stackoverflow.com/a/1429275 :)
        Set<String> menus = new HashSet<>();
        CodeSource src = permGUI.getClass().getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            try {
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null)
                        break;
                    String name = e.getName();
                    if (name.startsWith("menus/") && !name.equals("menus/")) {
                        menus.add(name);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return menus;
    }

    public FileConfiguration createPremade(String name) {
        File menuFolder = new File(permGUI.getDataFolder(), "premades" + File.separator);
        if (!menuFolder.exists()) {
            menuFolder.mkdir();
        }
        File premadeFile = new File(menuFolder, name + ".yml");
        if (!premadeFile.exists()) {
            try {
                premadeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(premadeFile);
    }

    public void savePremade(String name, FileConfiguration config) {
        File menuFolder = new File(permGUI.getDataFolder(), "premades" + File.separator);
        if (!menuFolder.exists()) {
            menuFolder.mkdir();
        }
        File premadeFile = new File(menuFolder, name + ".yml");
        if (premadeFile.exists()) {
            try {
                config.save(premadeFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
