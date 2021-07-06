package me.snowman.permgui2.bot;

import me.snowman.permgui2.managers.BotManager;
import me.snowman.permgui2.managers.FileManager;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;

public class Listeners extends ListenerAdapter {
    private final BotManager botManager;
    private final FileManager fileManager;

    public Listeners(BotManager botManager, FileManager fileManager){
        this.botManager = botManager;
        this.fileManager = fileManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(!event.isFromType(ChannelType.PRIVATE)) return;
        if(event.getMessage().getAuthor().isBot()) return;
        int codesSize = botManager.getLinkCodes().size();
        for(Map.Entry<UUID, String> entry : botManager.getLinkCodes().entrySet()){
            if(entry.getValue().equals(event.getMessage().getContentRaw())){
                fileManager.getPlayer(Bukkit.getPlayer(entry.getKey())).set("ID", event.getMessage().getAuthor().getId());
                fileManager.savePlayer();
                Bukkit.getPlayer(entry.getKey()).sendMessage("Linked");
                event.getChannel().sendMessage("Linked").queue();
                botManager.getLinkCodes().remove(entry.getKey());
                return;
            }
        }
        if(codesSize == botManager.getLinkCodes().size()){
            event.getChannel().sendMessage("Cod invalid").queue();
        }
    }
}
