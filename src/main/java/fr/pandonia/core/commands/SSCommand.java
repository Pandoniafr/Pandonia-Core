package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.core.menus.sanction.SanctionMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSCommand implements CommandExecutor {

        private PandoniaAPI instance;

        public SSCommand(PandoniaAPI instance) {
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

                if (args.length == 1) {

                        new SanctionMenu(args[0]).open(p);

                }else{
                        p.sendMessage(PandoniaMessages.getPrefix() + "§fMauvaise utilisation | §3/ss §f(§3pseudo§f)");
                        return false;
                }

                return false;
        }
}
