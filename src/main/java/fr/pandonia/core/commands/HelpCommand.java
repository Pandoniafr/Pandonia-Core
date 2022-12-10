package fr.pandonia.core.commands;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.commands.IPandoniaPermission;
import fr.pandonia.api.messages.PandoniaMessages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand implements CommandExecutor {

    private PandoniaAPI instance;

    public HelpCommand(PandoniaAPI instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

        List<IPandoniaPermission> commands = instance.getCommandsManager().getPlayerAllowedCommands(s).stream().filter(permission -> permission.getCommand().getDescription() != null && !permission.getCommand().getDescription().isEmpty()).collect(Collectors.toList());
        long n = 10;
        int page = Math.max(args.length > 0 && StringUtils.isNumeric(args[0]) ? Integer.parseInt(args[0]) : 1, 1);
        long fromIndex = n * (page - 1);
        long toIndex = n * page;
        long size = commands.size() / n + 1;
        if(page > size){
            s.sendMessage(PandoniaMessages.getPrefix() + "Il n'y a que " + size + " page" + (page > 1 ? "s" : "") + " dans la liste.");
        }else{
            s.sendMessage("§8§m--------------------------------");
            s.sendMessage("");
            s.sendMessage("  §8┃ §f§lAide - Page " + page + "/" + size);
            commands.stream().sorted(Comparator.comparing(permission -> permission.getCommand().getLabel())).skip(fromIndex).limit(toIndex - fromIndex).forEach(permission -> {
                s.sendMessage("    §8» §3/" + permission.getCommand().getLabel() + " §f» §7" + permission.getCommand().getDescription() + (permission.getGroup().getCustomName() !=  null ? " §r(" + permission.getGroup().getCustomName() + "§r)" : ""));
            });
            s.sendMessage("");
            s.sendMessage("§8§m--------------------------------");
        }

        return true;
    }
}
