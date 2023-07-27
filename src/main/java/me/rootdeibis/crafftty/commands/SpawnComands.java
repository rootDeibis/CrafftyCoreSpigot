package me.rootdeibis.crafftty.commands;

import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommandLoader;
import org.bukkit.command.CommandSender;

@CoreCommandLoader
public class SpawnComands {

    @CoreCommand(name = "setspawn", permission = "core.cmd.setspawn", aliases = {"setlobby"})
    public void setSpawn(CommandSender sender, String label, String[] args) {
        sender.sendMessage("Holis");
    }
}
