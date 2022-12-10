package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NHCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String msg, String[] args) {
        if(s instanceof Player){
            IPPlayer player = PandoniaAPI.get().getPlayerManager().getPlayer(((Player) s).getUniqueId());
            if(player != null && player.getRankObject(PandoniaAPI.get().getRankManager()).getPower() >= PandoniaAPI.get().getRankManager().getRank("MOD").getPower()){
                s.sendMessage(PandoniaMessages.getPrefix() + "§fHistorique de pseudos de §3" + s.getName() + " §f:");

            }
        }
        return false;
    }
}
