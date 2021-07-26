package me.snowman.permgui2.managers;

import me.snowman.permgui2.objects.User;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private final FileManager fileManager;
    public UserManager(FileManager fileManager){
        this.fileManager = fileManager;
    }

    private final List<User> users = new ArrayList<>();

    public User getUser(Player player) {
        for (User user : users) {
            if (user.getPlayer().getUniqueId().equals(player.getUniqueId())) return user;
        }
        User user = new User(player);
        user.setDiscordID(fileManager.getPlayer(player).getString("ID"));
        users.add(user);
        return user;
    }
}
