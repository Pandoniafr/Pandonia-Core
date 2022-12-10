package fr.pandonia.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FriendsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String msg, String[] args) {
        if(cmd.getName().equalsIgnoreCase("friends") || cmd.getName().equalsIgnoreCase("f")){
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("list")){
                    //LIST
                    return true;
                }

            }
        }
        return false;
    }
}
