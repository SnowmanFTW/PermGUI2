package me.snowman.permgui2.objects;

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

    public Set<String> getPerms(String group) {
        return groupPerms.get(group);
    }

    public Set<String> getGroups() {
        return groupPerms.keySet();
    }


}
