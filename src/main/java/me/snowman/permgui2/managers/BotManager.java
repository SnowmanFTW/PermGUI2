package me.snowman.permgui2.managers;

import me.snowman.permgui2.bot.Listeners;
import me.snowman.permgui2.PermGUI;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BotManager {
    private final PermGUI permGUI;
    private final FileManager fileManager;

    private JDABuilder builder;
    private JDA bot;

    private final Map<UUID, String> linkCodes = new HashMap<>();
    private final Map<UUID, String> linkIDs = new HashMap<>();

    public BotManager(PermGUI permGUI, FileManager fileManager){
        this.permGUI = permGUI;
        this.fileManager = fileManager;
    }

    public void startBot(){
        Bukkit.getScheduler().runTaskAsynchronously(permGUI, () -> {
            Bukkit.getServer().getConsoleSender().sendMessage("Bot starting...");
            try {
                bot = builder.addEventListeners(new Listeners(this, fileManager)).build();
                bot.awaitReady();
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
            Bukkit.getServer().getConsoleSender().sendMessage("Bot started");
        });
    }

    public void stopBot(){
        if(bot == null){
            Bukkit.getServer().getConsoleSender().sendMessage("Bot never started, this is probably an error.");
            return;
        }
        bot.shutdown();
        Bukkit.getServer().getConsoleSender().sendMessage("Bot stopped");
    }

    public void createRole(String name){
        Bukkit.getScheduler().runTaskAsynchronously(permGUI, () -> {
            bot.getGuilds().get(0).createRole().setName(name).submit(true);
        });
    }

    public void setRole(String role){
        Bukkit.getScheduler().runTaskAsynchronously(permGUI, () -> {
        });
    }

    public void setColor(String role, String prefix){
        ChatColor last = ChatColor.getByChar(prefix.charAt(prefix.lastIndexOf(ChatColor.COLOR_CHAR) + 1));
        bot.getGuilds().get(0).getRolesByName(role, false).get(0).getManager().setColor(last.getColor()).queue();
    }

    public void setToken(String token){
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
}
