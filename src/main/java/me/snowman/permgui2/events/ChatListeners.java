package me.snowman.permgui2.events;

import me.snowman.permgui2.managers.MessageManager;
import me.snowman.permgui2.managers.PermsManager;
import me.snowman.permgui2.managers.UserManager;
import me.snowman.permgui2.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;

public class ChatListeners implements Listener {
    private final MessageManager messageManager;
    private final PermsManager permsManager;
    private final UserManager userManager;

    public ChatListeners(MessageManager messageManager, PermsManager permsManager, UserManager userManager) {
        this.messageManager = messageManager;
        this.permsManager = permsManager;
        this.userManager = userManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = messageManager.color(event.getMessage());
        User user = userManager.getUser(player);
        if (!user.isChat()) return;
        event.setCancelled(true);

        if (message.equals("cancel")) {
            player.sendMessage(messageManager.getMessages("Cancel"));
            user.removeChat();
            return;
        }
        String action = user.getChat();
        String targetString = user.getTarget();
        if (action.toLowerCase().contains("prefix")) {
            if (Bukkit.getServer().getPlayer(targetString) != null) {
                Player target = Bukkit.getServer().getPlayer(targetString);
                if (message.equals("none")) {
                    permsManager.getChat().setPlayerPrefix(null, target, null);
                    player.sendMessage(messageManager.getMessages("PrefixRemoved"));
                    return;
                }
                permsManager.getChat().setPlayerPrefix(null, target, message);
            } else if (Arrays.stream(permsManager.getPerms().getGroups()).map(group -> group.equals(user.getTarget())).anyMatch(aBoolean -> true)) {
                if (message.equals("none")) {
                    permsManager.getChat().setGroupPrefix((World) null, targetString, null);
                    player.sendMessage(messageManager.getMessages("PrefixRemoved"));
                    return;
                }
                permsManager.getChat().setGroupPrefix((World) null, targetString, message);
            }
            player.sendMessage(messageManager.getMessages("PrefixSet").replace("%prefix%", message));
        } else {
            if (Bukkit.getServer().getPlayer(targetString) != null) {
                Player target = Bukkit.getServer().getPlayer(targetString);
                if (message.equals("none")) {
                    permsManager.getChat().setPlayerSuffix(null, target, null);
                    player.sendMessage(messageManager.getMessages("SuffixRemoved"));
                    return;
                }
                permsManager.getChat().setPlayerSuffix(null, target, message);
            } else if (Arrays.stream(permsManager.getPerms().getGroups()).map(group -> group.equals(user.getTarget())).anyMatch(aBoolean -> true)) {
                if (message.equals("none")) {
                    permsManager.getChat().setGroupSuffix((World) null, targetString, null);
                    player.sendMessage(messageManager.getMessages("SuffixRemoved"));
                    return;
                }
                permsManager.getChat().setGroupSuffix((World) null, targetString, message);
            }
            player.sendMessage(messageManager.getMessages("SuffixSet").replace("%suffix%", message));
        }

        user.removeChat();
    }
}
