package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import me.snowman.permgui2.objects.Premade;
import me.snowman.permgui2.objects.User;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;

public class PremadeManager {
    private final PermGUI permGUI;
    private final FileManager fileManager;
    private final PermsManager permsManager;
    private final MessageManager messageManager;

    public PremadeManager(PermGUI permGUI, FileManager fileManager, PermsManager permsManager, MessageManager messageManager) {
        this.permGUI = permGUI;
        this.fileManager = fileManager;
        this.permsManager = permsManager;
        this.messageManager = messageManager;
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

    public void createPremade(String name, User user) {
        FileConfiguration premadeFile = fileManager.createPremade(name);
        int max = permsManager.getPerms().getGroups().length;
        if (!permsManager.isLuckPerms()) return;
        int i = 0;
        for (String groups : permsManager.getPerms().getGroups()) {
            Group group = permsManager.getLuckPerms().getGroupManager().getGroup(groups);
            List<String> perms = group.getNodes(NodeType.PERMISSION).stream()
                    .map(PermissionNode::getPermission)
                    .collect(Collectors.toList());
            premadeFile.set(groups, perms);
            i++;
            user.sendActionBar(messageManager.getPercent(i, max));
        }
        fileManager.savePremade(name, premadeFile);
    }
    
    public void loadPremade(Premade premade) {
        for (String groups : premade.getGroups()) {
            for (String perms : premade.getPerms(groups)) {
                permsManager.getPerms().groupAdd((World) null, groups, perms);
            }
        }
    }
}
