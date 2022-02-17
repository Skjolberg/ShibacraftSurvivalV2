package net.shibacraft.shibacraft.commands;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.types.SuffixNode;
import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.fileManager.FileManager;
import net.shibacraft.shibacraft.playerManager.PlayerManager;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Presidente implements CommandExecutor {
    private final Shibacraft plugin;
    private final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private final PlayerManager playerManager;

    public Presidente(Shibacraft plugin, PlayerManager playerManager) {

        this.plugin = plugin;
        this.playerManager = playerManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command ciudadano, String label, String[] args) {

        FileManager messagesFile = new FileManager(plugin, "messages");
        FileManager ciudadesFile = new FileManager(plugin, "ciudades");
        FileConfiguration config = plugin.getConfig();

        Player user = (Player) sender;
        List<String> ciudadanos = ciudadesFile.getStringList(user.getName() + ".ciudadanos");
        final String prefix = messagesFile.getString("Prefix");


        if (args.length > 0) {
            User userLP = luckPermsAPI.getPlayerAdapter(Player.class).getUser(user);
            if (args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("user") && args.length > 2) {
                if (!ciudadesFile.contains(user.getName())) {
                    if (messagesFile.getString("CreateCityFirst").length() > 0) {
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                messagesFile.getString("CreateCityFirst").replace("{prefix}", prefix)));
                    }
                    return true;
                } else {
                    for (String i : ciudadanos) {
                        if (args[2].equalsIgnoreCase(i)) {
                            String suffix = "&f[&b" + ciudadesFile.getString(user.getName() + ".displayname") + "&f]&r";
                            UserManager userManager = luckPermsAPI.getUserManager();

                            OfflinePlayer player = Bukkit.getOfflinePlayer(i);
                            CompletableFuture<User> userFuture = userManager.loadUser(player.getUniqueId());

                            userFuture.thenAcceptAsync(userI -> {
                                removeSuffix(userI, suffix);
                            });

                            ciudadanos.remove(ciudadanos.indexOf(i));
                            ciudadesFile.set(user.getName() + ".ciudadanos", ciudadanos);
                            ciudadesFile.set(user.getName() + ".restante", ciudadesFile.getInt(user.getName() + ".restante") + 1);
                            ciudadesFile.save();
                            ciudadesFile.reload();
                            if (messagesFile.getString("CitizenKicked").length() > 0) {
                                user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        messagesFile.getString("CitizenKicked").replace("{prefix}", prefix)));
                            }
                            return true;
                        }
                    }
                    if (messagesFile.getString("CitizenNoFromYourCity").length() > 0) {
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                messagesFile.getString("CitizenNoFromYourCity").replace("{prefix}", prefix)));
                    }
                }
                return true;
            } else if (args[0].equalsIgnoreCase("list")) {
                if (ciudadanos.isEmpty()) {
                    if (messagesFile.getString("ListEmpty").length() > 0) {
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                messagesFile.getString("ListEmpty").replace("{prefix}", prefix)));
                    }
                    return true;
                } else {
                    if (messagesFile.getString("CitizenListHeader").length() > 0) {
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                messagesFile.getString("CitizenListHeader").replace("{prefix}", prefix)));
                        user.sendMessage("");
                    }
                    int cont = 1;
                    for (String i : ciudadanos) {
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + cont + ".&f " + i));
                        cont++;
                    }
                    if (messagesFile.getString("CitizenListFooter").length() > 0) {
                        user.sendMessage("");
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                messagesFile.getString("CitizenListFooter").replace("{prefix}", prefix)));
                    }
                }
            } else if (args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("user") && args.length > 2) {
                //String name = Bukkit.getServer().getPlayer(UUID).getName();
                //Player invited = Bukkit.getServer().getPlayer(args[2]);

                if (!ciudadesFile.contains(user.getName())) {
                    user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                messagesFile.getString("CreateCityFirst").replace("{prefix}", prefix)));
                    return true;
                } else if (ciudadesFile.getInt(user.getName() + ".restante") == 0){
                    user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            messagesFile.getString("MaxCitizens").replace("{prefix}", prefix)));
                    return true;
                } else if (Bukkit.getServer().getPlayer(args[2]) != null){
                    Player invited = Bukkit.getServer().getPlayer(args[2]);

                    if(playerManager.isPendingPlayer(invited.getUniqueId())){
                        Player presidente = Bukkit.getPlayer(playerManager.getPresidentUUID(invited.getUniqueId()));
                        if(presidente.getName().equals(user.getName())){
                            user.sendMessage("Ya has invitado a este usuario.");

                        } else if (!presidente.getName().equals(user.getName())){
                            user.sendMessage("Este usuario ha sido invitado por otra ciudad.");

                        }
                        return true;
                    }

                    List<String> sectionList;
                    for (String i : ciudadesFile.getKeys(false)) {
                        sectionList = ciudadesFile.getStringList(i + ".ciudadanos");
                        for (String j : sectionList) {
                            if (args[2].equals(j)) {
                                if (messagesFile.getString("AlreadyHasCity").length() > 0) {
                                    user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                            messagesFile.getString("AlreadyHasCity").replace("{prefix}", prefix)));
                                    return true;
                                }
                            }
                        }
                    }
                    invited.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFile.getString("HasBeenInvited").replace("{city}", ciudadesFile.getString(user.getName()+".nombre")).replace("{prefix}", prefix).replace("{president}", user.getName())));
                    user.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFile.getString("InvitationSent").replace("{citizen}", invited.getName()).replace("{prefix}", prefix)));

                    playerManager.addPendingPlayer(invited.getUniqueId(), user.getUniqueId());

                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        try {
                            // Seconds
                            int n = 1000 * config.getInt("Expire");
                            Thread.sleep(n);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        playerManager.getRequests().clear();
                    });
                    return true;

                } else {
                    if (messagesFile.getString("Offline").length() > 0) {
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                messagesFile.getString("Offline").replace("{prefix}", prefix)));
                    }
                }
                return true;

            } else if (args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("city") && args.length > 2) {
                if (args[2].contains("&")) {
                    for (int i = 0; i < args[2].length() - 1; i++) {
                        if (args[2].charAt(i) == '&') {
                            if (args[2].charAt(i + 1) == 'k' || args[2].charAt(i + 1) == 'l' || args[2].charAt(i + 1) == 'm' || args[2].charAt(i + 1) == 'n' || args[2].charAt(i + 1) == 'o' && messagesFile.getString("InvalidCityName").length() > 0) {
                                user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        messagesFile.getString("InvalidCityName").replace("{prefix}", prefix)));
                                return true;
                            }
                        }
                    }
                }
                if (args[2].length() <= 12) {
                    if (!ciudadesFile.contains(user.getName())) {
                        for (String i : ciudadesFile.getKeys(false)) {
                            if (ciudadesFile.getString(i + ".nombre").equalsIgnoreCase(args[2]) && messagesFile.getString("CityAlreadyAdded").length() > 0) {
                                user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        messagesFile.getString("CityAlreadyAdded").replace("{prefix}", prefix)));
                                return true;
                            }
                        }
                        if (messagesFile.getString("CitySuccessfullyAdded").length() > 0) {
                            user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    messagesFile.getString("CitySuccessfullyAdded").replace("{prefix}", prefix)));
                        }
                        ciudadanos.add(user.getName());
                        ciudadesFile.set(user.getName() + ".nombre", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', Utils.STRIP_AMPERSAND_COLORS.matcher(args[2]).replaceAll(""))));
                        ciudadesFile.set(user.getName() + ".displayname", args[2]);
                        ciudadesFile.set(user.getName() + ".ciudadanos", ciudadanos);
                        ciudadesFile.set(user.getName() + ".restante", config.getInt("Citizens"));
                        ciudadesFile.save();
                        ciudadesFile.reload();
                        String suffix = "&f[&b" + args[2] + "&f]&r";
                        addSuffix(userLP, suffix);
                    } else {
                        if (messagesFile.getString("NoChangeNameCity").length() > 0) {
                            user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    messagesFile.getString("NoChangeNameCity").replace("{prefix}", prefix)));
                        }
                    }
                } else {
                    if (messagesFile.getString("InvalidCityLength").length() > 0) {
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                messagesFile.getString("InvalidCityLength").replace("{prefix}", prefix)));
                    }
                }
            } else {
                commandUsage(user, messagesFile);
            }
        } else {
            commandUsage(user, messagesFile);
        }
        return true;
    }

    public void addSuffix(User user, String suffix) {
        user.data().add(SuffixNode.builder(suffix, 1).build());
        luckPermsAPI.getUserManager().saveUser(user);
    }

    public void removeSuffix(User user, String suffix) {
        user.data().remove(SuffixNode.builder(suffix, 1).build());
        luckPermsAPI.getUserManager().saveUser(user);
    }

    public void commandUsage(Player p, FileManager f) {
        List<String> usageCommand;
        usageCommand = f.getStringList("UsagePresident");
        if (!usageCommand.isEmpty()) {
            for (String i : usageCommand) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        i));
            }
        }
    }
}