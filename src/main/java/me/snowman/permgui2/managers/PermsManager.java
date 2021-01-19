package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import me.snowman.permgui2.permissions.PermissionAction;
import me.snowman.permgui2.permissions.PermissionModify;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermsManager {
    private final PermGUI permGUI;
    private static Permission perms = null;
    private static Chat chat = null;
    private final List<PermissionModify> permissionModifyList = new ArrayList<>();
    public PermsManager(PermGUI permGUI){
        this.permGUI = permGUI;
    }

    public boolean setupPermissions() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getServer().getConsoleSender().sendMessage("&cVault &4not found. Install &cVault &4then try again.");
            Bukkit.getServer().getPluginManager().disablePlugin(permGUI);
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if(rsp == null){
            Bukkit.getServer().getConsoleSender().sendMessage("&cPermission plugin &4not found. Install a &cpermission plugin &4then try again.");
            Bukkit.getServer().getPluginManager().disablePlugin(permGUI);
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }

    public boolean setupChat() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getServer().getConsoleSender().sendMessage("&cVault &4not found. Install &cVault &4then try again.");
            Bukkit.getServer().getPluginManager().disablePlugin(permGUI);
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if(rsp == null){
            Bukkit.getServer().getConsoleSender().sendMessage("&cChat plugin &4not found. Install a &cchat plugin &4then try again.");
            Bukkit.getServer().getPluginManager().disablePlugin(permGUI);
            return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }
    
    public Permission getPerms(){
        return perms;
    }
    
    public Chat getChat(){
        return chat;
    }

    public void createPermissionModify(Player player, Player target, PermissionAction action, String string, boolean waiting){
        PermissionModify permissionModify = new PermissionModify(player, target, action, string, waiting);
        if(getPermissionModify(player) != null) permissionModifyList.remove(getPermissionModify(player));
        permissionModifyList.add(permissionModify);
    }

    public void createPermissionModify(Player player, Player target, PermissionAction action, String string){
        PermissionModify permissionModify = new PermissionModify(player, target, action, string);
        if(getPermissionModify(player) != null) permissionModifyList.remove(getPermissionModify(player));
        permissionModifyList.add(permissionModify);
    }

    public void createPermissionModify(Player player, Player target){
        PermissionModify permissionModify = new PermissionModify(player, target);
        if(getPermissionModify(player) != null) permissionModifyList.remove(getPermissionModify(player));
        permissionModifyList.add(permissionModify);
    }

    public void createPermissionModify(Player player, String group, PermissionAction action, String string, boolean waiting){
        PermissionModify permissionModify = new PermissionModify(player, group, action, string, waiting);
        if(getPermissionModify(player) != null) permissionModifyList.remove(getPermissionModify(player));
        permissionModifyList.add(permissionModify);
    }

    public void createPermissionModify(Player player, String group, PermissionAction action, String string){
        PermissionModify permissionModify = new PermissionModify(player, group, action, string);
        if(getPermissionModify(player) != null) permissionModifyList.remove(getPermissionModify(player));
        permissionModifyList.add(permissionModify);
    }

    public void createPermissionModify(Player player, String group){
        PermissionModify permissionModify = new PermissionModify(player, group);
        if(getPermissionModify(player) != null) permissionModifyList.remove(getPermissionModify(player));
        permissionModifyList.add(permissionModify);
    }

    public PermissionModify getPermissionModify(Player player){
        for(PermissionModify permissionModify: permissionModifyList){
            if(permissionModify.getPlayer().getName().equals(player.getName())){
                return permissionModify;
            }
        }
        return null;
    }

    public void action(PermissionModify permissionModify){
        PermissionAction action = permissionModify.getAction();
        Player target = permissionModify.getTarget();
        String group = permissionModify.getGroup();
        String string = permissionModify.getString();
        switch(action){
            case ADDGROUP:
                getPerms().playerAddGroup(null, target, string);
                break;
            case CHANGEGROUP:
                Arrays.stream(getPerms().getPlayerGroups(null, target)).forEach(groups -> getPerms().playerRemoveGroup(null, target, groups));
                getPerms().playerAddGroup(null, target, string);
                break;
            case PREFIX:
                if(target != null) getChat().setPlayerPrefix(null, target, string);
                else if(group != null) getChat().setGroupPrefix((World) null, group, string);
                break;
            case SUFFIX:
                if(target != null) getChat().setPlayerSuffix(null, target, string);
                else if(group != null) getChat().setGroupSuffix((World) null, group, string);
                break;
            case CREATEGROUP:
                if(Bukkit.getServer().getPluginManager().isPluginEnabled("LuckPerms")){
                    LuckPerms api = LuckPermsProvider.get();
                    api.getGroupManager().createAndLoadGroup(string);
                }
                break;
        }
    }
            
}
