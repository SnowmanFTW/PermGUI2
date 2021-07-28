package me.snowman.permgui2.bot;

import me.snowman.permgui2.PermGUI;
import me.snowman.permgui2.managers.BotManager;
import me.snowman.permgui2.managers.FileManager;
import me.snowman.permgui2.managers.MessageManager;
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
    private final MessageManager messageManager;
    private final PermGUI permGUI;

    public Listeners(BotManager botManager, FileManager fileManager, UserManager userManager, PermGUI permGUI, MessageManager messageManager){
        this.botManager = botManager;
        this.fileManager = fileManager;
        this.userManager = userManager;
        this.messageManager = messageManager;
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
                    player.sendMessage(messageManager.getMessages("LinkedMinecraft").replace("%discord%", event.getMessage().getAuthor().getAsTag()));
                    event.getChannel().sendMessage(messageManager.getRawMessage("LinkedDiscord").replace("%minecraft%", player.getName())).submit(true);
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
