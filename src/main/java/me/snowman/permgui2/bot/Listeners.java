package me.snowman.permgui2.bot;

import me.snowman.permgui2.PermGUI;
import me.snowman.permgui2.managers.BotManager;
import me.snowman.permgui2.managers.FileManager;
import me.snowman.permgui2.managers.UserManager;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class Listeners extends ListenerAdapter {
    private final BotManager botManager;
    private final FileManager fileManager;
    private final UserManager userManager;
    private final PermGUI permGUI;

    public Listeners(BotManager botManager, FileManager fileManager, UserManager userManager, PermGUI permGUI){
        this.botManager = botManager;
        this.fileManager = fileManager;
        this.userManager = userManager;
        this.permGUI = permGUI;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(permGUI, () -> {
            if (!event.isFromType(ChannelType.PRIVATE)) return;
            if (event.getMessage().getAuthor().isBot()) return;
            int codesSize = botManager.getLinkCodes().size();
            for (Map.Entry<UUID, String> entry : botManager.getLinkCodes().entrySet()) {
                if (entry.getValue().equals(event.getMessage().getContentRaw())) {
                    Player player = Bukkit.getPlayer(entry.getKey());
                    fileManager.getPlayer(player).set("ID", event.getMessage().getAuthor().getId());
                    fileManager.savePlayer();
                    userManager.getUser(player).setDiscordID(event.getMessage().getAuthor().getId());
                    Bukkit.getPlayer(entry.getKey()).sendMessage("Linked");
                    event.getChannel().sendMessage("Linked").submit(true);
                    botManager.getLinkCodes().remove(entry.getKey());
                    return;
                }
            }
            if (codesSize == botManager.getLinkCodes().size()) {
                event.getChannel().sendMessage("Cod invalid").submit(false);
            }
        });
    }
}
