package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

    private PandoniaAPI instance;

    public GamemodeCommand(PandoniaAPI instance) {
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

        if(args.length == 1) {

            if (args[0].equalsIgnoreCase("0")) {

                p.setGameMode(GameMode.SURVIVAL);

            } else if (args[0].equalsIgnoreCase("1")) {

                p.setGameMode(GameMode.CREATIVE);

            } else if (args[0].equalsIgnoreCase("2")) {

                p.setGameMode(GameMode.ADVENTURE);

            } else if (args[0].equalsIgnoreCase("3")) {

                p.setGameMode(GameMode.SPECTATOR);

            } else {

                p.sendMessage(PandoniaMessages.getPrefix() + "§fUn problème ? §f(§30§f/§31§f/§32§f/§33§f) §7§o[pseudo]");

            }

            return true;

        }if(args.length == 2) {

            Player target = Bukkit.getPlayer(args[1]);

            if (target != null){

                if (args[0].equalsIgnoreCase("0")) {

                    target.setGameMode(GameMode.SURVIVAL);

                } else if (args[0].equalsIgnoreCase("1")) {

                    target.setGameMode(GameMode.CREATIVE);

                } else if (args[0].equalsIgnoreCase("2")) {

                    target.setGameMode(GameMode.ADVENTURE);

                } else if (args[0].equalsIgnoreCase("3")) {

                    target.setGameMode(GameMode.SPECTATOR);

                } else {

                    target.sendMessage(PandoniaMessages.getPrefix() + "§fUn problème ? §f(§30§f/§31§f/§32§f/§33§f) §7§o[pseudo]");

                }

                return true;

            }

        } else {

            p.sendMessage(PandoniaMessages.getPrefix() + "§fUn problème ? §f(§30§f/§31§f/§32§f/§33§f) §7§o[pseudo]");

        }

        return false;
    }
}
