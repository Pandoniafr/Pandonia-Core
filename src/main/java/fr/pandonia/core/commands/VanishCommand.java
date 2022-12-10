package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.events.PlayerVanishEvent;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.player.settings.IPlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String msg, String[] args) {
        if(s instanceof Player){
            IPPlayer player = PandoniaAPI.get().getPlayerManager().getPlayer(((Player) s).getUniqueId());
            if(player != null && player.getRankObject(PandoniaAPI.get().getRankManager()).getPower() >= PandoniaAPI.get().getRankManager().getRank("MOD").getPower()){
                IPlayerSettings settings = PandoniaAPI.get().getPlayerSettingsManager().getPlayerSettings(player.getUUID());
                if(settings != null){
                    PlayerVanishEvent event = new PlayerVanishEvent((Player) s, !settings.isVanish());
                    Bukkit.getPluginManager().callEvent(event);
                    if(!event.isCancelled()){
                        settings.setVanish(event.isEnabled());
                        s.sendMessage(PandoniaMessages.getPrefix() + "Vous venez " + (event.isEnabled() ? "§ad'activer" : "de §cdésactiver") + "§r le mode §3vanish§r.");
                    }
                }
            }
        }
        return false;
    }
}
