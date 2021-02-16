package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import me.snowman.permgui2.objects.Premade;
import me.snowman.permgui2.objects.User;
import net.luckperms.api.actionlog.ActionLog;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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
        YamlConfiguration premade = getPremadeFile(fileName);
        Map<String, Set<String>> premades = new HashMap<>();
        for (String groups : premade.getKeys(false)) {
            Set<String> allPerms = new TreeSet<>();
            allPerms.addAll(premade.getStringList(groups));
            premades.put(groups, allPerms);

        }
        return new Premade().setGroupPerms(premades);
    }


    public Set<String> getPremades() {
        Set<String> premades = new HashSet<>();
        File premadesFolder = new File(permGUI.getDataFolder(), "premades");
        if(premadesFolder.listFiles() == null) return null;
        for (File premadeFile : premadesFolder.listFiles()) {
            premades.add(premadeFile.getName());
        }
        return premades;
    }

    public YamlConfiguration getPremadeFile(String fileName) {
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
    
    public void loadPremade(Premade premade, User user, boolean wipe) {
        int max = premade.getGroups().size();
        int i = 0;
        for (String groups : premade.getGroups()) {
            CompletableFuture<ActionLog> logFuture = permsManager.getLuckPerms().getActionLogger().getLog();
            if(permsManager.getLuckPerms().getGroupManager().getGroup(groups) == null){
                permsManager.getLuckPerms().getGroupManager().createAndLoadGroup(groups);
            }
            logFuture.join();
            if(wipe) wipeGroup(groups);
            for (String perms : premade.getPerms(groups)) {
                permsManager.getPerms().groupAdd((World) null, groups, perms);
            }
            i++;
            user.sendActionBar(messageManager.getPercent(i, max));
        }
    }

    public void wipeGroup(String string){
        Group group = permsManager.getLuckPerms().getGroupManager().getGroup(string);
        for(Node node: group.getNodes(NodeType.PERMISSION)){
            DataMutateResult result = group.data().remove(node);
        }
        permsManager.getLuckPerms().getGroupManager().saveGroup(group);
    }
}
