package fr.pandonia.core.commands.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class APITabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String msg, String[] args) {
        List<String> tab = new ArrayList<>();
        if(args.length == 1){
            tab.add("scoreboardreload");
            tab.add("rankreload");
            tab.add("cosmeticsreload");
            tab.add("queue");
            tab.add("whitelist");
        }
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("queue") || args[0].equalsIgnoreCase("whitelist")){
                tab.add("on");
                tab.add("off");
            }
        }
        return args.length == 0 ? new ArrayList<>() : StringUtil.copyPartialMatches(args[args.length-1], tab, new ArrayList<>());
    }
}