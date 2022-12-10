package fr.pandonia.core.listeners.player;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.commands.PandoniaCommandGroup;
import fr.pandonia.api.events.packets.PacketReadEvent;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.core.commands.LagCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R3.PacketPlayOutTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.help.HelpTopic;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerCommandListener implements Listener {

    private PandoniaCore instance;

    public PlayerCommandListener(PandoniaCore instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void tps(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if(e.getMessage().toLowerCase().startsWith("/tps")){
            p.sendMessage("§8§m----------------------------------");
            p.sendMessage("");
            p.sendMessage(" §8»§f Serveur: §3" + PandoniaAPI.get().getInstanceInfo().getName());
            p.sendMessage(" §8»§f Date: §e" + new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").format(new Date(System.currentTimeMillis())) + " (Heure locale)");
            p.sendMessage("");
            final int ping = ((CraftPlayer) p).getHandle().ping;
            p.sendMessage(" §8»§f Ping: §" +  (ping < 200 ? 'a' : ping < 500 ? '6' : 'c') + ping + "ms");
            double[] tps = ((CraftServer) Bukkit.getServer()).getServer().recentTps;
            p.sendMessage(" §8»§f TPS: §3" + LagCommand.formatTps(tps[0]) + "§f, " + LagCommand.formatTps(tps[1]) + "§f, " + LagCommand.formatTps(tps[2]));
            p.sendMessage("");
            p.sendMessage("§8§m----------------------------------");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!event.isCancelled()) {
            String command = event.getMessage();
            if(command.contains(" ")){
                command = event.getMessage().split(" ")[0];
            }
            if(!instance.getCommandsManager().getPlayerAllowedCommandLabels(event.getPlayer()).contains(command.replace("/", "")) && !event.getPlayer().isOp()){
                player.sendMessage(PandoniaMessages.getPrefix() + "§fCommande introuvable...");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandRestrict(PlayerCommandPreprocessEvent event) {

        if (!event.isCancelled()) {

            Player player = event.getPlayer();
            String cmd = event.getMessage().split(" ")[0];
            HelpTopic topic = Bukkit.getServer().getHelpMap().getHelpTopic(cmd);

            if (topic == null) {
                player.sendMessage(PandoniaMessages.getPrefix() + "§fCommande introuvable...");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTabComplete(PacketReadEvent e){
        if(e.getPacket() instanceof PacketPlayInTabComplete){
            if(e.getPlayer().isOp()){
                return;
            }
            PacketPlayInTabComplete tab = (PacketPlayInTabComplete) e.getPacket();
            String message = tab.a();
            if(message.startsWith("/")){
                if(!message.contains(" ")){
                    e.setCancelled(true);
                    List<String> commands = new ArrayList<>();
                    /*.getCommandsManager().getPermissions().stream().filter(permission -> permission.getGroup().canSee(e.getPlayer())).forEach(permission -> {
                        commands.add("/" + permission.getCommand().getLabel());
                        if(permission.getCommand().getAliases() != null){
                            commands.forEach(s -> commands.add("/" + s));
                        }
                    });*/
                    instance.getCommandsManager().getPlayerSeeCommands(e.getPlayer()).forEach(iPandoraPermission -> {
                        commands.add("/" + iPandoraPermission.getCommand().getLabel());
                        if(iPandoraPermission.getCommand().getAliases() != null){
                            iPandoraPermission.getCommand().getAliases().forEach(s -> commands.add("/" + s));
                        }
                    });
                    List<String> currentCommands = new ArrayList<>();
                    StringUtil.copyPartialMatches(message, commands.stream().sorted().collect(Collectors.toList()), currentCommands);
                    if(!currentCommands.isEmpty()){
                        PacketPlayOutTabComplete tabComplete = new PacketPlayOutTabComplete(currentCommands.toArray(new String[0]));
                        ((CraftPlayer)e.getPlayer()).getHandle().playerConnection.sendPacket(tabComplete);
                    }
                }else{
                    String label = message.split(" ")[0].replace("/", "");
                    if(!instance.getCommandsManager().playerCanSee(e.getPlayer(), label)){
                        e.setCancelled(true);
                    }
                }

            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        PermissionAttachment pa = e.getPlayer().addAttachment(instance.getPlugin());
        for (PandoniaCommandGroup group : instance.getCommandsManager().getGroups()) {
            pa.setPermission("pandonia." + group.getName(), true);
        }
    }

}
