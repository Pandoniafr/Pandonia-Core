package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.events.PlayerNickEvent;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.core.menus.nick.NickMenu;
import fr.pandonia.core.player.nick.Nick;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCmd implements CommandExecutor {

    private PandoniaCore instance;

    public NickCmd(PandoniaCore instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        IPPlayer pplayer = PandoniaAPI.get().getPlayerManager().getPlayer(p.getUniqueId());

        if(pplayer.getRankObject(instance.getRankManager()).getPower() < instance.getRankManager().getRank("MOD").getPower()) {
            p.sendMessage(PandoniaMessages.getNoPerms());
            return false;
        }
        if(args.length > 0) {
            String nick = args[0];
            if(nick.equalsIgnoreCase("off")) {
                PlayerNickEvent playerNickEvent = new PlayerNickEvent(p);
                Bukkit.getPluginManager().callEvent(playerNickEvent);
                if(!playerNickEvent.isCancelled()){
                    Bukkit.getScheduler().runTaskAsynchronously(instance.getPlugin(), () -> {
                        pplayer.setNick(null);
                        instance.getNickManager().updateNick(p, true);
                    });
                }
            }else if(nick.equalsIgnoreCase("skin")){
                if(args.length > 1){
                    if(pplayer.getNick() != null){
                        PlayerNickEvent playerNickEvent = new PlayerNickEvent(p);
                        Bukkit.getPluginManager().callEvent(playerNickEvent);
                        if(!playerNickEvent.isCancelled()){
                            Bukkit.getScheduler().runTaskAsynchronously(instance.getPlugin(), () -> {
                                pplayer.getNick().setSkinName(args[1]);
                                instance.getNickManager().updateNick(p, true);
                            });
                        }
                    }
                }
            }else if(nick.length() > 3 && nick.length() < 16){
                PlayerNickEvent playerNickEvent = new PlayerNickEvent(p);
                Bukkit.getPluginManager().callEvent(playerNickEvent);
                if(!playerNickEvent.isCancelled()){
                    Bukkit.getScheduler().runTaskAsynchronously(instance.getPlugin(), () -> {
                        pplayer.setNick(new Nick(nick));
                        instance.getNickManager().updateNick(p, false);
                        instance.getProxyLink().updatePlayerNick(pplayer);
                    });
                }
            }else{
                p.sendMessage(PandoniaMessages.getPrefix() + "Votre nick doit contenir entre 4 et 16 caractères.");
            }
        }else{
            new NickMenu().open(p);
        }

        return false;
    }
}
