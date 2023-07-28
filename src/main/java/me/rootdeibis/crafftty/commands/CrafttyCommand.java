package me.rootdeibis.crafftty.commands;

import me.rootdeibis.crafftty.commands.core.SubCommands;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommandLoader;
import me.rootdeibis.crafftty.core.commands.annotations.CoreSubCommands;
import me.rootdeibis.crafftty.core.commands.annotations.TabCompletion;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@CoreCommandLoader
public class CrafttyCommand {

    @CoreCommand(name = "craftty", permission = "core.cmd.main", aliases = {"core"})
    @CoreSubCommands(list = {SubCommands.class})
    public void coreCommand(String[] args, CommandSender sender) {

    }

    @TabCompletion(target = TabCompletion.TargetType.MAIN, command = "craftty")
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        return new ArrayList<>();
    }
}
