package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LagCommand implements CommandExecutor {

    private PandoniaAPI instance;

    public LagCommand(PandoniaAPI instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("§8§m----------------------------------");
        sender.sendMessage("");
        sender.sendMessage(" §8»§f Serveur: §3" + PandoniaAPI.get().getInstanceInfo().getName());
        sender.sendMessage(" §8»§f Date: §e" + new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").format(new Date(System.currentTimeMillis())) + " (Heure locale)");
        sender.sendMessage("");
        if(sender instanceof Player) {
            final int ping = ((CraftPlayer) sender).getHandle().ping;
            sender.sendMessage(" §8»§f Ping: §" +  (ping < 200 ? 'a' : ping < 500 ? '6' : 'c') + ping + "ms");
        }
        double[] tps = ((CraftServer) Bukkit.getServer()).getServer().recentTps;
        sender.sendMessage(" §8»§f TPS: §3" + formatTps(tps[0]) + "§f, " + formatTps(tps[1]) + "§f, " + formatTps(tps[2]));
        sender.sendMessage("");
        sender.sendMessage("§8§m----------------------------------");
        return true;
    }

    public static String formatTps(double tps) {
        return "§" + (tps > 18 ? 'a' : tps > 15 ? '6' : 'c') + String.format("%1.2f", tps);
    }

}
