package me.snowman.permgui2.managers;

import me.snowman.permgui2.bot.Listeners;
import me.snowman.permgui2.PermGUI;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.internal.entities.UserById;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class BotManager {
    private final PermGUI permGUI;
    private final FileManager fileManager;
    private final UserManager userManager;
    private final MessageManager messageManager;

    private JDABuilder builder;
    private JDA bot;

    private final Map<UUID, String> linkCodes = new HashMap<>();
    private final Map<UUID, String> linkIDs = new HashMap<>();

    public BotManager(PermGUI permGUI, FileManager fileManager, UserManager userManager, MessageManager messageManager){
        this.permGUI = permGUI;
        this.fileManager = fileManager;
        this.userManager = userManager;
        this.messageManager = messageManager;
    }

    public void startBot(){
        if(!fileManager.getConfig().getBoolean("Discord.Enabled")) return;
        Bukkit.getScheduler().runTaskAsynchronously(permGUI, () -> {
            Bukkit.getServer().getConsoleSender().sendMessage("Bot starting...");
            try {
                bot = builder.addEventListeners(new Listeners(this, fileManager, userManager, permGUI, messageManager)).build();
                bot.awaitReady();
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
            Bukkit.getServer().getConsoleSender().sendMessage("Bot started");
        });
    }

    public void stopBot(){
        if(!fileManager.getConfig().getBoolean("Discord.Enabled")) return;
        if(bot == null){
            Bukkit.getServer().getConsoleSender().sendMessage("Bot never started, this is probably an error.");
            return;
        }
        bot.shutdown();
        Bukkit.getServer().getConsoleSender().sendMessage("Bot stopped");
    }

    public void createRole(String name){
        if(!fileManager.getConfig().getBoolean("Discord.Enabled")) return;
        Bukkit.getScheduler().runTaskAsynchronously(permGUI, () -> {
            getGuild().createRole().setName(name).submit(true);
        });
    }

    public void setRole(String role, me.snowman.permgui2.objects.User user){
        if(!fileManager.getConfig().getBoolean("Discord.Enabled")) return;
        Bukkit.getScheduler().runTaskAsynchronously(permGUI, () -> {
            if(user.getDiscordID() == null) return;
            Member discordUser = null;
            try {
                discordUser = getGuild().retrieveMemberById(user.getDiscordID()).complete(true);
            } catch (RateLimitedException e) {
                e.printStackTrace();
            }
            if(discordUser == null) return;
            if(getGuild().getRolesByName(role, true).size() == 0) createRole(role);
            getGuild().addRoleToMember(discordUser, getGuild().getRolesByName(role, true).get(0)).submit(true);
        });
    }

    public void removeRole(String role, me.snowman.permgui2.objects.User user){
        if(!fileManager.getConfig().getBoolean("Discord.Enabled")) return;
        Bukkit.getScheduler().runTaskAsynchronously(permGUI, () -> {
            if(user.getDiscordID() == null) return;
            Member discordUser = null;
            try {
                discordUser = getGuild().retrieveMemberById(user.getDiscordID()).complete(true);
            } catch (RateLimitedException e) {
                e.printStackTrace();
            }
            if(discordUser == null) return;
            if(getGuild().getRolesByName(role, true).size() == 0) return;
            getGuild().removeRoleFromMember(discordUser, getGuild().getRolesByName(role, true).get(0)).submit(true);
        });
    }

    public void setColor(String role, String prefix){
        if(!fileManager.getConfig().getBoolean("Discord.Enabled")) return;
        ChatColor last = ChatColor.getByChar(prefix.charAt(prefix.lastIndexOf(ChatColor.COLOR_CHAR) + 1));
        bot.getGuilds().get(0).getRolesByName(role, false).get(0).getManager().setColor(last.getColor()).submit(true);
    }

    public void setToken(String token){
        if(!fileManager.getConfig().getBoolean("Discord.Enabled")) return;
        builder = JDABuilder.createDefault(token);
    }

    public JDA getBot() {
        return bot;
    }

    public Map<UUID, String> getLinkCodes() {
        return linkCodes;
    }

    public Map<UUID, String> getLinkIDs() {
        return linkIDs;
    }

    public Guild getGuild(){
        return bot.getGuilds().get(0);
    }
}