package me.rootdeibis.crafftty.commands;

import me.rootdeibis.crafftty.CrafttySpigotCore;
import me.rootdeibis.crafftty.commands.core.SubCommands;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommandLoader;
import me.rootdeibis.crafftty.core.commands.annotations.CoreSubCommands;
import me.rootdeibis.crafftty.core.commands.annotations.TabCompletion;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CoreCommandLoader
public class CrafttyCommand {

    @CoreCommand(name = "craftty", permission = "core.cmd.main", aliases = {"core"})
    @CoreSubCommands(list = {SubCommands.class})
    public boolean coreCommand(String[] args, CommandSender sender) {
        return false;
    }

    @TabCompletion(target = TabCompletion.TargetType.MAIN, command = "craftty")
    public List<String> onTabCompleteMain(CommandSender sender, String label, String[] args) {
        return Arrays.asList("reload", "protection");
    }
    @TabCompletion(target = TabCompletion.TargetType.SUBCOMMAND, command = "craftty", subcommand = "protection")
    public List<String> onTabCompleteProtection(CommandSender sender, String label, String[] args) {
        if(args.length == 2) {
            return new ArrayList<>(CrafttySpigotCore.getFileManager().use("settings.yml").getConfigurationSection("spawn.disable").getKeys(false));
        }

        return Collections.emptyList();
    }
}
