package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private PandoniaAPI instance;

    public FlyCommand(PandoniaAPI instance) {
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

        if(args.length < 1) {

            if(!p.getAllowFlight()) {

                p.sendMessage(PandoniaMessages.getPrefix() + "§fVous venez §ad'activer §fle §3fly§f.");
                p.setAllowFlight(true);

            } else {

                p.sendMessage(PandoniaMessages.getPrefix() + "§fVous venez de §3désactiver §fle §3fly§f.");
                p.setAllowFlight(false);

            }
        }

        return false;
    }
}
