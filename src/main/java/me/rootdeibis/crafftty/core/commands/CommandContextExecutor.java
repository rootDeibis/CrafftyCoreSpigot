package me.rootdeibis.crafftty.core.commands;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.rootdeibis.crafftty.CrafttySpigotCore;
import me.rootdeibis.crafftty.manager.Files.RFile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.function.Consumer;

public class CommandContextExecutor extends BukkitCommand {

    private final CommandContext commandContext;

    protected CommandContextExecutor(CommandContext commandContext) {
        super(commandContext.getCommand().name());

        this.commandContext = commandContext;
    }

    @FunctionalInterface
    private interface Function<In1, Out> {
        Out apply(In1 in1);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        RFile file = CrafttySpigotCore.getFileManager().use("messages.yml");

        CommandContextExecutor.Function<String, Boolean> needPermission = (p) -> p != null && p.length() > 0 && !commandSender.hasPermission(p);

        if(needPermission.apply(this.commandContext.getCommand().permission())) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getString("commands.no-permission")));
        } else {
            if(this.commandContext.getSubCommands().size() == 0 || args.length == 0) {

                this.invokeMethod(this.commandContext.getCommandMethod(), this.commandContext.getCommandClass(), commandSender, s, args);
            } else {

                this.commandContext.getSubCommands().stream().filter(d -> d.getSubcommand().name().equalsIgnoreCase(args[0]) || Arrays.stream(d.getSubcommand().aliases()).anyMatch(ds -> ds.equalsIgnoreCase(args[0]))).findFirst().ifPresent(sub -> {

                    if(needPermission.apply(sub.getSubcommand().permission())) {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getString("commands.no-permission")));
                    } else {


                        this.invokeMethod(sub.getSubcommandMethod(), sub.getSubcommandClass(), commandSender, sub.getSubcommand().name(), Arrays.stream(args, args.length == 1 ? 0 : 1, args.length).toArray(String[]::new));

                    }

                });

            }
        }
        return false;
    }

    public void invokeMethod(Method method, Object instance,CommandSender commandSender, String s, String[] strings) {
        try {

            Function<Integer, Object> resolve = (pos) -> this.resolveParameter(method, pos, commandSender, s, strings);


            method.invoke(instance, resolve.apply(0), resolve.apply(1), resolve.apply(2));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    private Object resolveParameter(Method method, int paremeterPos,CommandSender commandSender, String s, String[] strings) {

        Parameter parameter = method.getParameters()[paremeterPos];

        if(parameter != null) {
            if(parameter.getType().isInstance(commandSender)) {
                return commandSender;
            } else if(parameter.getType().isInstance(s)) {
                return s;
            } else if(parameter.getType().isInstance(strings)) {
                return strings;
            } else {
                return null;
            }
        }

        return null;

    }
}
