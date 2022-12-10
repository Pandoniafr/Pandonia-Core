package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {

    private PandoniaAPI instance;

    public SpeedCommand(PandoniaAPI instance) {
        this.instance = instance;
    }

    //Un level de speed = +4 de walkspeed

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        IPPlayer pplayer = PandoniaAPI.get().getPlayerManager().getPlayer(p.getUniqueId());

        if (pplayer.getRankObject(instance.getRankManager()).getPower() < instance.getRankManager().getRank("MOD").getPower()) {
            p.sendMessage(PandoniaMessages.getNoPerms());
            return false;
        }

        if (args.length == 0) {
            p.sendMessage(PandoniaMessages.getPrefix() + " §fMauvaise utilisation | §3/speed <1/10>");
            return false;
        }

        if (args.length == 1) {

            int value = Integer.parseInt(args[0]);

            if (value <= 100 && value >= 1){
                p.setWalkSpeed(value/100f);
                p.sendMessage(PandoniaMessages.getPrefix() + "§fVous venez de définir votre §bvitesse §fà §b" + value + "§f.");
            }
        }
        return false;
    }
}
