package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.player.owning.IPlayerOwning;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.core.scoreboard.manager.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class APICommand implements CommandExecutor {

    private PandoniaAPI instance;

    public APICommand(PandoniaAPI instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        IPPlayer pplayer = PandoniaAPI.get().getPlayerManager().getPlayer(p.getUniqueId());

        if (pplayer.getRankObject(instance.getRankManager()).isAdmin()){
            if (args.length == 1){
                if (args[0].equalsIgnoreCase("scoreboardreload")){
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        PandoniaCore.getCore().getScoreboardManager().onLogout(onlinePlayer);
                    }
                    PandoniaCore.getCore().scoreboardManager = new ScoreboardManager(PandoniaCore.getCore());
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        PandoniaCore.getCore().getScoreboardManager().onLogin(onlinePlayer);
                    }
                    p.sendMessage(PandoniaMessages.getPrefix() + "§fLe scoreboard a été reload");
                }
                else if (args[0].equalsIgnoreCase("rankreload")){
                    instance.getRankManager().register();
                    p.sendMessage(PandoniaMessages.getPrefix() + "§fLes ranks ont été reload");
                }else if (args[0].equalsIgnoreCase("cosmeticsreload")){
                    instance.getCosmeticsManager().registerCosmetics();
                    p.sendMessage(PandoniaMessages.getPrefix() + "§fLes cosmetics ont été reload");
                }else{
                    p.sendMessage(PandoniaMessages.getErreur() + "§fMauvaise utilisation");
                }
            }else if (args.length == 2){
                if (args[0].equalsIgnoreCase("queue")){
                    if (args[1].equalsIgnoreCase("on")){
                        PandoniaAPI.get().getProxyLink().setQueueStatus(true);
                        p.sendMessage(PandoniaMessages.getPrefix() + "§fVous avez activé la file d'attente");
                    }else if (args[1].equalsIgnoreCase("off")){
                        PandoniaAPI.get().getProxyLink().setQueueStatus(false);
                        p.sendMessage(PandoniaMessages.getPrefix() + "§fVous avez désactivé la file d'attente");
                    }
                }else if (args[0].equalsIgnoreCase("whitelist")){
                    if (args[1].equalsIgnoreCase("on")){
                        PandoniaAPI.get().getProxyLink().setWhitelistStatus(true);
                        p.sendMessage(PandoniaMessages.getPrefix() + "§fVous avez activé la whitelist");
                    }else if (args[1].equalsIgnoreCase("off")){
                        PandoniaAPI.get().getProxyLink().setWhitelistStatus(false);
                        p.sendMessage(PandoniaMessages.getPrefix() + "§fVous avez désactivée la whitelist");
                    }
                }
            }else if (args.length == 3){
                if (args[0].equalsIgnoreCase("givecosmetic")){
                    IPPlayer pp = instance.getPlayerManager().getPlayer(args[1]);
                    if (pp != null){
                        int id = Integer.parseInt(args[2]);
                        if (id >= -1){
                            IPlayerOwning owning = instance.getPlayerOwningManager().getPlayerOwning(pp.getUUID());
                            if (owning != null){
                                owning.getPlayerCosmeticsOwning().addCosmetic(id);
                                p.sendMessage("§fVous venez de give le cosmetic " + id + " a " + pp.getName());
                            }
                        }
                    }else{
                        p.sendMessage("§fLe joueur doit être connecté et sur le même serveur que vous");
                    }
                }
            }else{
                p.sendMessage(PandoniaMessages.getErreur() + "§fMauvaise utilisation");
            }
        }else{
            p.sendMessage(PandoniaMessages.getNoPerms());
        }

        return false;
    }
}
