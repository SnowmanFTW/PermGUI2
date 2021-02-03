package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import me.snowman.permgui2.objects.Premade;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class PremadeManager {
    private final PermGUI permGUI;
    private final FileManager fileManager;
    private final PermsManager permsManager;

    public PremadeManager(PermGUI permGUI, FileManager fileManager, PermsManager permsManager) {
        this.permGUI = permGUI;
        this.fileManager = fileManager;
        this.permsManager = permsManager;
    }

    public Premade getPremade(String fileName) {
        FileConfiguration premade = getPremadeFile(fileName);
        Map<String, Set<String>> premades = new HashMap<>();
        for (String groups : premade.getKeys(false)) {
            for (String perms : premade.getStringList(groups)) {
                premades.get(groups).add(perms);
            }
        }
        return new Premade().setGroupPerms(premades);
    }


    public Set<String> getPremades() {
        Set<String> premades = new HashSet<>();
        File premadesFolder = new File(permGUI.getDataFolder(), "premades");
        for (File premadeFile : premadesFolder.listFiles()) {
            premades.add(premadeFile.getName());
        }
        return premades;
    }

    public FileConfiguration getPremadeFile(String fileName) {
        File premadeFile = new File(permGUI.getDataFolder(), "premades" + File.separator + fileName);
        if (!premadeFile.exists()) {
            getServer().getConsoleSender().sendMessage(fileName + " doesn't exist!");
            return null;
        }
        return YamlConfiguration.loadConfiguration(premadeFile);
    }

    public void createPremade(String name) {
        FileConfiguration premadeFile = fileManager.createPremade(name);

    }
}
