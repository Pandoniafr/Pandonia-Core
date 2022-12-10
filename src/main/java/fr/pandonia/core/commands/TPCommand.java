package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCommand implements CommandExecutor {

    private PandoniaAPI instance;

    public TPCommand(PandoniaAPI instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        IPPlayer pplayer = PandoniaAPI.get().getPlayerManager().getPlayer(p.getUniqueId());

        if (pplayer.getRankObject(instance.getRankManager()).getPower() < instance.getRankManager().getRank("MOD").getPower()) {
            p.sendMessage(PandoniaMessages.getNoPerms());
            return false;
        }

        if (args.length == 0) {
            p.sendMessage(PandoniaMessages.getPrefix() + " §fMauvaise utilisation | §3/tp §f(§3pseudo§f §8| §r§3x y z§f)");
            return false;
        }

        if (args.length == 1) {

            Player target = Bukkit.getPlayer(args[0]);

            if (target != null) {

                p.teleport(target.getLocation());
                p.sendMessage(PandoniaMessages.getPrefix() + "§fVous venez de vous téléporter à §3" + target.getName() + "§f.");

            } else {
                p.sendMessage(PandoniaMessages.getPrefix() + "§fCe §3joueur§f n'est pas §3connecté§f ou §3inconnu§f de nos serveurs... ");
            }
        }else if (args.length == 2) {

            Player target = Bukkit.getPlayer(args[0]);
            Player target2 = Bukkit.getPlayer(args[1]);

            if (target != null) {
                if (target2 != null) {

                    target.teleport(target2.getLocation());
                    p.sendMessage(PandoniaMessages.getPrefix() + "§fVous venez de téléporter §3" + target.getName() + " §fà §3" + target2.getName() + "§f.");

                    target.sendMessage(PandoniaMessages.getPrefix() + "§fVous venez de vous faire téléporter à §3" + target2.getName() + "§f.");
                    target2.sendMessage(PandoniaMessages.getPrefix() + "§3" + target.getName() + " §fvient de se faire téléporter à vous.");

                } else {
                    p.sendMessage(PandoniaMessages.getPrefix() + "§fLe §3deuxieme joueur§f n'est pas §3connecté§f ou §3inconnu§f de nos serveurs... ");
                }
            } else {
                p.sendMessage(PandoniaMessages.getPrefix() + "§fLe §3premier joueur§f n'est pas §3connecté§f ou §3inconnu§f de nos serveurs... ");
            }
        }else if (args.length == 3){

            int x;
            int y;
            int z;

            try {
                x = Integer.parseInt(args[0]);
                y = Integer.parseInt(args[1]);
                z = Integer.parseInt(args[2]);
            }catch (NumberFormatException e){
                p.sendMessage(PandoniaMessages.getPrefix() + " §fMauvaise utilisation | §3/tp §f(§3pseudo§f §8| §r§3x y z§f)");
                return false;
            }

            p.sendMessage(PandoniaMessages.getPrefix() + "§fVous venez de vous téléporter en §3" + x + " " + y + " " + z + "§f.");
            p.teleport(new Location(p.getWorld(), x, y, z));
            return true;

        }

        return false;
    }
}
