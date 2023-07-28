package me.rootdeibis.crafftty.commands.core;


import me.rootdeibis.crafftty.CrafttySpigotCore;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;
import me.rootdeibis.crafftty.manager.Files.RFile;

import me.rootdeibis.crafftty.manager.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SubCommands {

    @CoreCommand(name = "protection", permission = "core.subcmd.protection", aliases = "protect")
    public void subcommandProtection(CommandSender sender, String[] args) {

        RFile messages = CrafttySpigotCore.getFileManager().use("settings.yml");
        String types = String.join(", ", messages.getConfigurationSection("spawn.disable").getKeys(false));
        Messages.Placeholders placeholders = new Messages.Placeholders();
        placeholders.add("types", types);

        if(args.length == 0) {
            sender.sendMessage(Messages.format("commands.protection.types",placeholders));
        }

    }

    @CoreCommand(name = "reload", permission = "core.subcmd.reload", aliases = "-r")
    public void onSubCommandReload(CommandSender sender) {
        CrafttySpigotCore.getFileManager().reload();

        sender.sendMessage(Messages.format("commands.reload", new Messages.Placeholders()));

    }


}
