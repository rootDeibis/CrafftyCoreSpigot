package me.rootdeibis.crafftty.commands;

import me.rootdeibis.crafftty.CrafttySpigotCore;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommandLoader;
import me.rootdeibis.crafftty.manager.Files.RFile;
import me.rootdeibis.crafftty.manager.Messages;
import me.rootdeibis.crafftty.manager.SpawnManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CoreCommandLoader
public class SpawnCommands {

    @CoreCommand(name = "setspawn", permission = "core.cmd.setspawn", aliases = {"setlobby"})
    public void setSpawn( String label, String[] args, CommandSender sender) {


        if(sender instanceof Player) {
            RFile messages = CrafttySpigotCore.getFileManager().use("messages.yml");

            SpawnManager.set(((Player) sender).getLocation());

            sender.sendMessage(Messages.format("commands.spawn.set", new Messages.Placeholders()));

        }


    }

    @CoreCommand(name = "spawn", permission = "core.cmd.spawn", aliases = {"lobby"})
    public void spawn(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player) {
            if(SpawnManager.isSpawnLaoded()) {
                RFile messages = CrafttySpigotCore.getFileManager().use("messages.yml");

                ((Player) sender).teleport(SpawnManager.getSpawnLocation());

                sender.sendMessage(Messages.format("commands.spawn.teleport", new Messages.Placeholders()));
            }
        }
    }
}
