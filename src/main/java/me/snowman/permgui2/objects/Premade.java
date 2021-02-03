package me.snowman.permgui2.objects;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Premade {
    private Map<String, Set<String>> groupPerms = new HashMap<>();

    public Premade() {
    }

    public Premade setGroupPerms(Map<String, Set<String>> groupPerms) {
        this.groupPerms = groupPerms;
        return this;
    }

    public Premade setGroupPerms(FileConfiguration file) {
        Map<String, Set<String>> premades = new HashMap<>();
        for (String groups : file.getKeys(false)) {
            for (String perms : file.getStringList(groups)) {
                premades.get(groups).add(perms);
            }
        }
        this.groupPerms = premades;
        return this;
    }

    public Set<String> getPerms(String group) {
        return groupPerms.get(group);
    }

    public Set<String> getGroups() {
        return groupPerms.keySet();
    }


}
