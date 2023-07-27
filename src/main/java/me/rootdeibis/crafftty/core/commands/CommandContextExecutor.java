package me.rootdeibis.crafftty.core.commands;

import me.rootdeibis.crafftty.CrafttySpigotCore;
import me.rootdeibis.crafftty.manager.Files.RFile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CommandContextExecutor extends BukkitCommand {

    private final CommandContext commandContext;

    protected CommandContextExecutor(CommandContext commandContext) {
        super(commandContext.getCommand().name());

        this.commandContext = commandContext;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        RFile file = CrafttySpigotCore.getFileManager().use("messages.yml");
        if(this.commandContext.getCommand().permission() != null && !commandSender.hasPermission(this.commandContext.getCommand().permission())) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getString("commands.no-permission")));
        } else {
            if(this.commandContext.getSubCommands().size() == 0) {

                this.invokeMethod(this.commandContext.getCommandMethod(), this.commandContext.getCommandClass(), commandSender, s, args);
            } else {

                this.commandContext.getSubCommands().stream().filter(d -> d.getSubcommand().name().equalsIgnoreCase(args[1])).findFirst().ifPresent(sub -> {

                    if(sub.getSubcommand().permission() != null && !commandSender.hasPermission(this.commandContext.getCommand().permission())) {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getString("commands.no-permission")));
                    } else {


                        this.invokeMethod(sub.getSubcommandMethod(), sub.getSubcommandClass(), commandSender, sub.getSubcommand().name(), Arrays.stream(args, 1, args.length).toArray(String[]::new));

                    }

                });

            }
        }
        return false;
    }

    public void invokeMethod(Method method, Object instance,CommandSender commandSender, String s, String[] strings) {
        try {
            method.invoke(instance, commandSender, s, strings);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


}
