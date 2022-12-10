package fr.pandonia.core.commands.manager;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.commands.ICommandsManager;
import fr.pandonia.api.commands.IPandoniaPermission;
import fr.pandonia.api.commands.PandoniaCommandGroup;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.core.commands.*;
import fr.pandonia.core.commands.permissions.PandoniaPermission;
import fr.pandonia.core.commands.tabcompleter.APITabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandsManager implements ICommandsManager {

    private final PandoniaAPI instance;
    private final List<IPandoniaPermission> permissions;
    private final List<PandoniaCommandGroup> groups;

    public CommandsManager(PandoniaAPI instance) {
        this.instance = instance;
        this.permissions = new ArrayList<>();
        this.groups = new ArrayList<>();
        registerGroups();
        Bukkit.getScheduler().runTaskLater(instance.getPlugin(), this::registerPermissions, 40);
    }

    @Override
    public void register(){

        instance.getPlugin().getCommand("opme").setExecutor(new OpMeCommand(instance));

        instance.getPlugin().getCommand("tp").setExecutor(new TPCommand(instance));

        instance.getPlugin().getCommand("fly").setExecutor(new FlyCommand(instance));

        instance.getPlugin().getCommand("gamemode").setExecutor(new GamemodeCommand(instance));

        instance.getPlugin().getCommand("tps").setExecutor(new LagCommand(instance));

        instance.getPlugin().getCommand("ss").setExecutor(new SSCommand(instance));

        instance.getPlugin().getCommand("api").setExecutor(new APICommand(instance));
        instance.getPlugin().getCommand("api").setTabCompleter(new APITabCompleter());

        instance.getPlugin().getCommand("pl").setExecutor(new PluginsCommand(instance));

        instance.getPlugin().getCommand("help").setExecutor(new HelpCommand(instance));

        instance.getPlugin().getCommand("nick").setExecutor(new NickCmd(PandoniaCore.getCore()));

        instance.getPlugin().getCommand("speed").setExecutor(new SpeedCommand(instance));

        instance.getPlugin().getCommand("vanish").setExecutor(new VanishCommand());
    }

    public void registerGroups(){
        registerGroup(new PandoniaCommandGroup("admin", 50, "§cAdmin") {
            @Override
            public boolean hasPermission(CommandSender s) {
                if(s instanceof HumanEntity){
                    IPPlayer pp = instance.getPlayerManager().getPlayer(((HumanEntity) s).getUniqueId());if(pp != null){
                        return pp.getRankObject(instance.getRankManager()).isAdmin();
                    }
                    return false;
                }
                return true;
            }
        });

        registerGroup(new PandoniaCommandGroup("mod", 30, "§3Mod") {
            @Override
            public boolean hasPermission(CommandSender s) {
                if(s instanceof HumanEntity){
                    IPPlayer pp = instance.getPlayerManager().getPlayer(((HumanEntity) s).getUniqueId());
                    if(pp != null){
                        return pp.getRankObject(instance.getRankManager()).getPower() >= instance.getRankManager().getRank("MOD").getPower();
                    }
                    return false;
                }
                return true;
            }
        });

        registerGroup(new PandoniaCommandGroup("staff", 20, "§2Staff") {
            @Override
            public boolean hasPermission(CommandSender s) {
                if(s instanceof HumanEntity){
                    IPPlayer pp = instance.getPlayerManager().getPlayer(((HumanEntity) s).getUniqueId());
                    if(pp != null){
                        return pp.getRankObject(instance.getRankManager()).isStaff();
                    }
                    return false;
                }
                return true;
            }
        });

        registerGroup(new PandoniaCommandGroup("player", 1) {
            @Override
            public boolean hasPermission(CommandSender s) {
                return true;
            }
        });
    }

    public void registerPermissions(){
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            for (Command command : PluginCommandYamlParser.parse(plugin)) {
                if(command != null){
                    if(command.getPermission() != null){
                        if(command.getPermission().startsWith("pandonia")){
                            PandoniaCommandGroup group = getGroup(command.getPermission().replace("pandonia.", ""));
                            if(group != null){
                                PandoniaPermission pp = new PandoniaPermission(command, group);
                                permissions.add(pp);
                            }else{
                                Bukkit.getConsoleSender().sendMessage("§3PANDONIA CORE §r: Groupe inconnu \"" + command.getPermission().replace("pandonia.", "") + "\" (Commande \"" + command.getLabel() + "\")");
                            }
                        }
                    }else if(plugin.getDescription().getMain().contains("pandonia")){
                        Bukkit.getConsoleSender().sendMessage("§3PANDONIA CORE§r : Permission non défini pour la commande " + command.getLabel() + " dans le plugin " + plugin.getName());
                    }
                }
            }
        }
    }

    @Override
    public List<String> getPlayerAllowedCommandLabels(CommandSender s){
        List<String> list = new ArrayList<>();
        getPlayerAllowedCommands(s).forEach(iPandoraPermission -> {
            list.add(iPandoraPermission.getCommand().getLabel());
            if(iPandoraPermission.getCommand().getAliases() != null){
                list.addAll(iPandoraPermission.getCommand().getAliases());
            }
        });
        return list;
    }

    @Override
    public List<IPandoniaPermission> getPermissions() {
        return permissions;
    }

    @Override
    public List<IPandoniaPermission> getPlayerAllowedCommands(CommandSender s){
        List<IPandoniaPermission> list = new ArrayList<>();
        if(s instanceof Player){
            Player p = (Player) s;
            IPPlayer pp = instance.getPlayerManager().getPlayer(p.getUniqueId());
            if(pp != null){
                list.addAll(permissions.stream().filter(iPandoraPermission -> iPandoraPermission.getGroup().hasPermission(p)).collect(Collectors.toList()));
            }
        }else{
            return new ArrayList<>(permissions);
        }
        return list;
    }

    @Override
    public List<IPandoniaPermission> getPlayerSeeCommands(CommandSender s){
        List<IPandoniaPermission> list = new ArrayList<>();
        if(s instanceof Player){
            Player p = (Player) s;
            IPPlayer pp = instance.getPlayerManager().getPlayer(p.getUniqueId());
            if(pp != null){
                list.addAll(permissions.stream().filter(iPandoraPermission -> iPandoraPermission.getGroup().canSee(p)).collect(Collectors.toList()));
            }
        }else{
            return new ArrayList<>(permissions);
        }
        return list;
    }

    @Override
    public List<PandoniaCommandGroup> getGroups() {
        return groups;
    }

    @Override
    public PandoniaCommandGroup getGroup(String name){
        for (PandoniaCommandGroup group : groups) {
            if(group.getName().equalsIgnoreCase(name)){
                return group;
            }
        }
        return null;
    }

    @Override
    public IPandoniaPermission getPermission(String label){
        for (IPandoniaPermission permission : permissions) {
            if(permission.getCommand().getLabel().equalsIgnoreCase(label)){
                return permission;
            }else{
                if(permission.getCommand().getAliases() != null){
                    if(permission.getCommand().getAliases().contains(label)){
                        return permission;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean playerHasPermission(Player p, String label){
        return getPlayerAllowedCommandLabels(p).contains(label);
    }

    @Override
    public boolean playerCanSee(Player p, String label){
        return getPermission(label) != null && getPermission(label).getGroup().canSee(p);
    }

    @Override
    public void registerGroup(PandoniaCommandGroup group){
        groups.add(group);
    }

}
