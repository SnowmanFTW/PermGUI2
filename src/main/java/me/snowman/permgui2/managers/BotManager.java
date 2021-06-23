package me.snowman.permgui2.managers;

import me.snowman.permgui2.PermGUI;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;

public class BotManager {
    private final PermGUI permGUI;

    private final JDABuilder builder = JDABuilder.createDefault("ODU2OTgxNDA5MzE4NTY3OTU2.YNI8IQ.C4jW56prfzIsciQzFG6K5Ah-eQ8");
    private JDA bot;

    public BotManager(PermGUI permGUI){
        this.permGUI = permGUI;
    }

    public void startBot(){
        Bukkit.getScheduler().runTaskAsynchronously(permGUI, () -> {
            try {
                bot = builder.build();
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

    public JDA getBot() {
        return bot;
    }
}
