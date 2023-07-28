package me.rootdeibis.crafftty.commands;

import me.rootdeibis.crafftty.commands.core.SubCommands;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommandLoader;
import me.rootdeibis.crafftty.core.commands.annotations.CoreSubCommands;
import org.bukkit.command.CommandSender;

@CoreCommandLoader
public class CrafttyCommand {

    @CoreCommand(name = "craftty", permission = "core.cmd.main", aliases = {"core"})
    @CoreSubCommands(list = {SubCommands.class})
    public void coreCommand(CommandSender sender) {

    }
}
