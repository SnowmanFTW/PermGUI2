package me.snowman.permgui2.events;

import me.snowman.permgui2.managers.*;
import me.snowman.permgui2.objects.Premade;
import me.snowman.permgui2.objects.User;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.*;
import java.util.Arrays;

public class ChatListeners implements Listener {
    private final MessageManager messageManager;
    private final PermsManager permsManager;
    private final UserManager userManager;
    private final PremadeManager premadeManager;
    private final BotManager botManager;

    public ChatListeners(MessageManager messageManager, PermsManager permsManager, UserManager userManager, PremadeManager premadeManager, BotManager botManager) {
        this.messageManager = messageManager;
        this.permsManager = permsManager;
        this.userManager = userManager;
        this.premadeManager = premadeManager;
        this.botManager = botManager;
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
        String action = user.getChat().toLowerCase();
        String targetString = user.getTarget();
        if (action.contains("prefix")) {
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
                botManager.setColor(targetString, message);
            }
            player.sendMessage(messageManager.getMessages("PrefixSet").replace("%prefix%", message));
        } else if (action.contains("suffix")) {
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
        } else if (action.contains("premade")) {
            if (targetString.equalsIgnoreCase("create")) {
                premadeManager.createPremade(message, user);
                player.sendMessage(messageManager.getMessages("PremadeSet").replace("%premade%", message));
            }
        } else if (action.contains("creategroup")){
            if(!permsManager.isLuckPerms()) return;
            permsManager.getLuckPerms().getGroupManager().createAndLoadGroup(message);
            player.sendMessage(messageManager.getMessages("GroupCreate").replace("%group%", message));
            botManager.createRole(message);
        }

        user.removeChat();
    }
}
