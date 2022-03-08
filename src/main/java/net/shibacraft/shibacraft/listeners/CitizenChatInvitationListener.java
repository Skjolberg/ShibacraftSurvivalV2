package net.shibacraft.shibacraft.listeners;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.dependencies.LuckPermsDependency;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.manager.players.PlayerInvitationManager;
import net.shibacraft.shibacraft.service.PresidenteService;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class CitizenChatInvitationListener implements Listener{

    private final Shibacraft plugin;
    private final PlayerInvitationManager playerManager;
    private final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private final LuckPermsDependency luckPermsDependency;
    private final PresidenteService presidenteService;

    public CitizenChatInvitationListener(Shibacraft plugin, PlayerInvitationManager playerManager, LuckPermsDependency luckPermsDependency, Utils utils, PresidenteService presidenteService) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.luckPermsDependency = luckPermsDependency;
        this.presidenteService = presidenteService;
    }

    @EventHandler()
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!playerManager.isPendingPlayer(player.getUniqueId())) {
            return;
        }

        if (event.getMessage().equalsIgnoreCase("confirmar")) {
            event.setCancelled(true);

            YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
            YamlManager messagesFile = new YamlManager(plugin, "messages");
            Player presidente = Bukkit.getPlayer(playerManager.getPresidentUUID(player.getUniqueId()));
            List<String> ciudadanos = presidenteService.getCitizensList(presidente);

            if (messagesFile.getString("InvitationAccepted").length() > 0) {
                player.sendMessage(Utils.toLegacyColors(messagesFile.getString("InvitationAccepted").replace("{prefix}", Utils.getPrefixMessages()).replace("{city}", ciudadesFile.getString(presidente.getName()+".nombre"))));
            }

            if (messagesFile.getString("HasAcceptedInvitation").length() > 0) {
                presidente.sendMessage(Utils.toLegacyColors(messagesFile.getString("HasAcceptedInvitation").replace("{prefix}", Utils.getPrefixMessages()).replace("{citizen}", player.getName())));
            }

            User userInvitedLP = luckPermsAPI.getPlayerAdapter(Player.class).getUser(player);
            luckPermsDependency.addSuffix(userInvitedLP, Utils.getSuffix(presidente.getName()));
            ciudadanos.add(player.getName());
            ciudadesFile.set(presidente.getName() + ".ciudadanos", ciudadanos);
            ciudadesFile.set(presidente.getName() + ".restante", ciudadesFile.getInt(presidente.getName() + ".restante") - 1);
            ciudadesFile.save();
            playerManager.removePendingPlayer(player.getUniqueId());
        }
    }
}

