package fr.pandonia.core.commands.permissions;

import fr.pandonia.api.commands.IPandoniaPermission;
import fr.pandonia.api.commands.PandoniaCommandGroup;
import org.bukkit.command.Command;

import java.util.List;

public class PandoniaPermission implements IPandoniaPermission {

    private Command command;
    private PandoniaCommandGroup group;
    private List<String> aliases;

    public PandoniaPermission(Command command, PandoniaCommandGroup group) {
        this.command = command;
        this.group = group;
    }

    @Override
    public Command getCommand() {
        return command;
    }

    @Override
    public PandoniaCommandGroup getGroup() {
        return group;
    }


}

