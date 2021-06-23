package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class BotManager {
    private final PermGUI permGUI;

    public BotManager(PermGUI permGUI){
        this.permGUI = permGUI;
    }

    private JDABuilder builder = null;
    private JDA bot;


    public void startBot(){
        Bukkit.getScheduler().runTaskAsynchronously(permGUI, () -> {
            try {
                bot = builder.build();
                bot.awaitReady();
            } catch (LoginException | InterruptedException e) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPermGUI2 &f- &bModify &fconfig.yml &band add a bot token for the bot to work!"));
                Bukkit.getLogger().severe("PermGUI2 - " + e.getMessage());
                return;
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

    public void setToken(String token){
        builder = JDABuilder.createDefault(token);
    }

    public JDA getBot() {
        return bot;
    }
}
