package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PluginsCommand implements CommandExecutor {

    private PandoniaAPI instance;

    public PluginsCommand(PandoniaAPI instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        IPPlayer pplayer = PandoniaAPI.get().getPlayerManager().getPlayer(p.getUniqueId());

        if (pplayer.getRankObject(instance.getRankManager()).isAdmin()){
            p.sendMessage(PandoniaMessages.getPrefix() + "§fListe des plugins : ");
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                StringBuilder sb = new StringBuilder();
                if(plugin.getDescription().getAuthors() != null){
                    plugin.getDescription().getAuthors().forEach(s -> sb.append(s).append(", "));
                }
                p.sendMessage(" - " + (plugin.isEnabled() ? (plugin.getDescription().getMain().contains("pandora") ? "§3" : "§a") : "§c") + plugin.getName() + (!plugin.getDescription().getAuthors().isEmpty() ? " §7(Développeur(s) : " + sb.substring(0, Math.max(sb.length()-2, 0)) + ")" : ""));
            }
        }else{
            p.sendMessage(PandoniaMessages.getNoPerms());
        }

        return false;
    }
}
