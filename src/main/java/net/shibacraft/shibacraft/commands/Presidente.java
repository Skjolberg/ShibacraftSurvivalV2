package net.shibacraft.shibacraft.commands;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.dependencies.LuckPermsDependency;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.manager.players.PlayerInvitationManager;
import net.shibacraft.shibacraft.service.PresidenteService;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Presidente implements CommandExecutor {
    private final Shibacraft plugin;
    private final net.luckperms.api.LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private final PlayerInvitationManager playerManager;
    private final PresidenteService presidenteService;
    private final LuckPermsDependency luckPermsDependency;

    public Presidente(Shibacraft plugin, PlayerInvitationManager playerManager, PresidenteService presidenteService, LuckPermsDependency luckPermsDependency) {

        this.plugin = plugin;
        this.playerManager = playerManager;
        this.presidenteService = presidenteService;
        this.luckPermsDependency = luckPermsDependency;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command presidenteCommand, String label, String[] args) {

        YamlManager messagesFile = new YamlManager(plugin, "messages");
        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
        FileConfiguration config = plugin.getConfig();
        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        if (!(sender instanceof Player user)) {
            Bukkit.getConsoleSender().sendMessage(Utils.toLegacyColors(
                    messagesFile.getString("ConsoleSender").replace("{prefix}", Utils.getPrefixMessages())));
        } else {
            if (args.length > 0) {
                User userLP = luckPermsAPI.getPlayerAdapter(Player.class).getUser(user);
                if (args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("user") && args.length > 2) {
                    if (!ciudadesFile.contains(user.getName())) {
                        if (messagesFile.getString("CreateCityFirst").length() > 0) {
                            user.sendMessage(Utils.toLegacyColors(
                                    messagesFile.getString("CreateCityFirst").replace("{prefix}", Utils.getPrefixMessages())));
                        }
                        return true;
                    } else {
                        for (String i : presidenteService.getCitizensList(user)) {
                            if (args[2].equalsIgnoreCase(i)) {
                                UserManager userManager = luckPermsAPI.getUserManager();

                                OfflinePlayer player = Bukkit.getOfflinePlayer(i);
                                CompletableFuture<User> userFuture = userManager.loadUser(player.getUniqueId());

                                userFuture.thenAcceptAsync(userI -> {
                                    luckPermsDependency.removeSuffix(userI, Utils.getSuffix(user.getName()));
                                });

                                ciudadesFile.set(user.getName() + ".ciudadanos", presidenteService.removeCitizen(user, i));
                                ciudadesFile.set(user.getName() + ".restante", ciudadesFile.getInt(user.getName() + ".restante") + 1);
                                ciudadesFile.save();
                                if (messagesFile.getString("CitizenKicked").length() > 0) {
                                    user.sendMessage(Utils.toLegacyColors(
                                            messagesFile.getString("CitizenKicked").replace("{prefix}", Utils.getPrefixMessages())));
                                }
                                return true;
                            }
                        }
                        if (messagesFile.getString("CitizenNoFromYourCity").length() > 0) {
                            user.sendMessage(Utils.toLegacyColors(
                                    messagesFile.getString("CitizenNoFromYourCity").replace("{prefix}", Utils.getPrefixMessages())));
                        }
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("city") && args.length == 2) {
                    if (!ciudadesFile.contains(user.getName())) {
                        if (messagesFile.getString("CreateCityFirst").length() > 0) {
                            user.sendMessage(Utils.toLegacyColors(
                                    messagesFile.getString("CreateCityFirst").replace("{prefix}", Utils.getPrefixMessages())));
                        }
                        return true;
                    } else {
                        for (String i : presidenteService.getCitizensList(user)) {
                            UserManager userManager = luckPermsAPI.getUserManager();

                            OfflinePlayer player = Bukkit.getOfflinePlayer(i);
                            CompletableFuture<User> userFuture = userManager.loadUser(player.getUniqueId());

                            userFuture.thenAcceptAsync(userI -> {
                                luckPermsDependency.removeSuffix(userI, Utils.getSuffix(user.getName()));
                            });

                        }
                        if (messagesFile.getString("CitySuccessfullyEliminated").length() > 0) {
                            user.sendMessage(Utils.toLegacyColors(
                                    messagesFile.getString("CitySuccessfullyEliminated").replace("{prefix}", Utils.getPrefixMessages())));
                        }
                        ciudadesFile.set(user.getName(), null);
                        ciudadesFile.save();
                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("list")) {
                    if (!ciudadesFile.contains(user.getName())) {
                        if (messagesFile.getString("ListEmpty").length() > 0) {
                            user.sendMessage(Utils.toLegacyColors(
                                    messagesFile.getString("CreateCityFirst").replace("{prefix}", Utils.getPrefixMessages())));
                        }
                        return true;
                    } else if (presidenteService.getCitizensList(user).isEmpty()){
                        if (messagesFile.getString("ListEmpty").length() > 0) {
                            user.sendMessage(Utils.toLegacyColors(
                                    messagesFile.getString("ListEmpty").replace("{prefix}", Utils.getPrefixMessages())));
                        }
                        return true;
                    }else {
                        presidenteService.viewCitizens(user);
                    }
                } else if (args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("user") && args.length > 2) {
                    if (!ciudadesFile.contains(user.getName())) {
                        user.sendMessage(Utils.toLegacyColors(
                                messagesFile.getString("CreateCityFirst").replace("{prefix}", Utils.getPrefixMessages())));
                    } else if (ciudadesFile.getInt(user.getName() + ".restante") == 0) {
                        user.sendMessage(Utils.toLegacyColors(
                                messagesFile.getString("MaxCitizens").replace("{prefix}", Utils.getPrefixMessages())));
                    } else if (Bukkit.getServer().getPlayer(args[2]) != null) {
                        Player invited = Bukkit.getServer().getPlayer(args[2]);
                        if (playerManager.isPendingPlayer(invited.getUniqueId())) {
                            Player presidente = Bukkit.getPlayer(playerManager.getPresidentUUID(invited.getUniqueId()));
                            if (presidente.getName().equals(user.getName())) {
                                user.sendMessage(Utils.toLegacyColors(
                                        messagesFile.getString("CitizenPending").replace("{prefix}", Utils.getPrefixMessages())));
                            } else if (!presidente.getName().equals(user.getName())) {
                                user.sendMessage(Utils.toLegacyColors(
                                        messagesFile.getString("CitizenPendingOther").replace("{prefix}", Utils.getPrefixMessages())));
                            }
                        }

                        List<String> sectionList;
                        for (String i : ciudadesFile.getKeys(false)) {
                            sectionList = ciudadesFile.getStringList(i + ".ciudadanos");
                            for (String j : sectionList) {
                                if (args[2].equals(j)) {
                                    if (messagesFile.getString("AlreadyHasCity").length() > 0) {
                                        user.sendMessage(Utils.toLegacyColors(
                                                messagesFile.getString("AlreadyHasCity").replace("{prefix}", Utils.getPrefixMessages())));
                                        return true;
                                    }
                                }
                            }
                        }
                        invited.sendMessage(Utils.toLegacyColors(messagesFile.getString("HasBeenInvited").replace("{city}", ciudadesFile.getString(user.getName() + ".nombre")).replace("{prefix}", Utils.getPrefixMessages()).replace("{president}", user.getName())));
                        user.sendMessage(Utils.toLegacyColors(messagesFile.getString("InvitationSent").replace("{citizen}", invited.getName()).replace("{prefix}", Utils.getPrefixMessages())));

                        playerManager.addPendingPlayer(invited.getUniqueId(), user.getUniqueId());
                        scheduler.runTaskLater(this.plugin, () -> playerManager.getRequests().remove(invited.getUniqueId(), user.getUniqueId()), config.getLong("Expire") * 20);
                        return true;
                    } else {
                        if (messagesFile.getString("Offline").length() > 0) {
                            user.sendMessage(Utils.toLegacyColors(
                                    messagesFile.getString("Offline").replace("{prefix}", Utils.getPrefixMessages())));
                        }
                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("city") && args.length > 2) {
                    if (args[2].contains("&")) {
                        for (int i = 0; i < args[2].length() - 1; i++) {
                            if (args[2].charAt(i) == '&') {
                                if (args[2].charAt(i + 1) == 'k' || args[2].charAt(i + 1) == 'l' || args[2].charAt(i + 1) == 'm' || args[2].charAt(i + 1) == 'n' || args[2].charAt(i + 1) == 'o' && messagesFile.getString("InvalidCityName").length() > 0) {
                                    user.sendMessage(Utils.toLegacyColors(
                                            messagesFile.getString("InvalidCityName").replace("{prefix}", Utils.getPrefixMessages())));
                                    return true;
                                }
                            }
                        }
                    }
                    if (args[2].length() <= 12) {
                        if (!ciudadesFile.contains(user.getName())) {
                            for (String i : ciudadesFile.getKeys(false)) {
                                if ((ciudadesFile.getString(i + ".nombre")).equalsIgnoreCase(args[2]) && messagesFile.getString("CityAlreadyAdded").length() > 0) {
                                    user.sendMessage(Utils.toLegacyColors(
                                            messagesFile.getString("CityAlreadyAdded").replace("{prefix}", Utils.getPrefixMessages())));
                                    return true;
                                }
                            }
                            if (messagesFile.getString("CitySuccessfullyAdded").length() > 0) {
                                user.sendMessage(Utils.toLegacyColors(
                                        messagesFile.getString("CitySuccessfullyAdded").replace("{prefix}", Utils.getPrefixMessages())));
                            }

                            ciudadesFile.set(user.getName() + ".nombre", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', Utils.STRIP_AMPERSAND_COLORS.matcher(args[2]).replaceAll(""))));
                            ciudadesFile.set(user.getName() + ".displayname", args[2]);
                            ciudadesFile.set(user.getName() + ".ciudadanos", presidenteService.addCitizen(user, user.getName()));
                            ciudadesFile.set(user.getName() + ".restante", config.getInt("Citizens"));
                            ciudadesFile.save();
                            luckPermsDependency.addSuffix(userLP, Utils.getSuffix(user.getName()));
                        } else {
                            if (messagesFile.getString("NoChangeNameCity").length() > 0) {
                                user.sendMessage(Utils.toLegacyColors(
                                        messagesFile.getString("NoChangeNameCity").replace("{prefix}", Utils.getPrefixMessages())));
                            }
                        }
                    } else {
                        if (messagesFile.getString("InvalidCityLength").length() > 0) {
                            user.sendMessage(Utils.toLegacyColors(
                                    messagesFile.getString("InvalidCityLength").replace("{prefix}", Utils.getPrefixMessages())));
                        }
                    }
                } else {
                    presidenteService.commandUsagePresidente(user, messagesFile);
                }
            } else {
                presidenteService.commandUsagePresidente(user, messagesFile);
            }
        }

        return true;
    }


}