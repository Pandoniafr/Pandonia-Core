package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpMeCommand implements CommandExecutor {

    private PandoniaAPI instance;

    public OpMeCommand(PandoniaAPI instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        IPPlayer pplayer = PandoniaAPI.get().getPlayerManager().getPlayer(p.getUniqueId());

        if (pplayer.getRankObject(instance.getRankManager()).getPower() < PandoniaAPI.get().getRankManager().getRank("MOD").getPower()) {
            p.sendMessage(PandoniaMessages.getNoPerms());
            return false;
        }

        if (!p.isOp()) {
            p.setOp(true);
            p.sendMessage(PandoniaMessages.getPrefix() + "§fVous êtes à présent §3opérateur§f.");
        } else {
            p.sendMessage(PandoniaMessages.getPrefix() + "§fVous êtes déjà §3opérateur§f.");
        }

        return false;
    }
}
