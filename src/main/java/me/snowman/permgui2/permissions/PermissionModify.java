package me.snowman.permgui2.permissions;

import me.snowman.permgui2.managers.PermsManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PermissionModify {
    PermsManager permsManager;
    Player player;
    Player target;
    String group;
    PermissionAction action;
    String string;
    boolean waiting;

    public PermissionModify(PermsManager permsManager){
        this.permsManager = permsManager;
    }

    public PermissionModify(Player player, Player target){
        this.player = player;
        this.target = target;
    }

    public PermissionModify(Player player, Player target, PermissionAction action, String string){
        this.player = player;
        this.target = target;
        this.action = action;
        this.string = string;
    }

    public PermissionModify(Player player, Player target, PermissionAction action, String string, boolean waiting){
        this.player = player;
        this.target = target;
        this.action = action;
        this.string = string;
        this.waiting = waiting;
    }

    public PermissionModify(Player player, String group){
        this.player = player;
        this.group = group;
    }

    public PermissionModify(Player player, String group, PermissionAction action, String string){
        this.player = player;
        this.group = group;
        this.action = action;
        this.string = string;
    }

    public PermissionModify(Player player, String group, PermissionAction action, String string, boolean waiting){
        this.player = player;
        this.group = group;
        this.action = action;
        this.string = string;
        this.waiting = waiting;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getTarget() {
        return target;
    }

    public String getGroup(){ return group;}

    public PermissionAction getAction() {
        return action;
    }

    public String getString(){
        return string;
    }

    public boolean isWaiting() {
        return waiting;
    }
}
