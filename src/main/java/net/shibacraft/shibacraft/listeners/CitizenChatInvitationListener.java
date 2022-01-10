package net.shibacraft.shibacraft.listeners;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.SuffixNode;
import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.fileManager.FileManager;
import net.shibacraft.shibacraft.playerManager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class CitizenChatInvitationListener implements Listener {

    private final Shibacraft plugin;
    private final PlayerManager playerManager;
    private final LuckPerms luckPermsAPI = LuckPermsProvider.get();

    public CitizenChatInvitationListener(Shibacraft plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    @EventHandler()
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!playerManager.isPendingPlayer(player.getUniqueId())) {
            return;
        }

        if (event.getMessage().equalsIgnoreCase("confirmar")) {
            event.setCancelled(true);

            FileManager ciudadesFile = new FileManager(plugin, "ciudades");
            FileManager messagesFile = new FileManager(plugin, "messages");
            Player presidente = Bukkit.getPlayer(playerManager.getPresidentUUID(player.getUniqueId()));
            final String prefix = messagesFile.getString("Prefix");
            List<String> ciudadanos = ciudadesFile.getStringList(presidente.getName() + ".ciudadanos");
            String suffix = "&f[&b" + ciudadesFile.getString(presidente.getName() + ".nombre") + "&f]&r";

            if (messagesFile.getString("InvitationAccepted").length() > 0) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',messagesFile.getString("InvitationAccepted")).replace("{prefix}", prefix).replace("{city}", ciudadesFile.getString(presidente.getName()+".nombre")));
            }

            if (messagesFile.getString("HasAcceptedInvitation").length() > 0) {
                presidente.sendMessage(messagesFile.getString("HasAcceptedInvitation").replace("{prefix}", prefix).replace("{citizen}", player.getName()));
            }

            User userInvitedLP = luckPermsAPI.getPlayerAdapter(Player.class).getUser(player);
            addSuffix(userInvitedLP, suffix);

            ciudadanos.add(player.getName());
            ciudadesFile.set(presidente.getName() + ".ciudadanos", ciudadanos);
            ciudadesFile.set(presidente.getName() + ".restante", ciudadesFile.getInt(presidente.getName() + ".restante") - 1);
            ciudadesFile.save();
            ciudadesFile.reload();
            playerManager.removePendingPlayer(player.getUniqueId());
        }
    }
    public void addSuffix(User user, String suffix) {
        user.data().add(SuffixNode.builder(suffix, 1).build());
        luckPermsAPI.getUserManager().saveUser(user);
    }
}

