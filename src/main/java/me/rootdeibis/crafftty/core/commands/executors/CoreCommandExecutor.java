package me.rootdeibis.crafftty.core.commands.executors;

import me.rootdeibis.crafftty.CrafttySpigotCore;
import me.rootdeibis.crafftty.core.commands.MethodUtils;
import me.rootdeibis.crafftty.core.commands.context.CommandContext;
import me.rootdeibis.crafftty.core.commands.context.SubCommandContext;
import me.rootdeibis.crafftty.manager.Files.RFile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoreCommandExecutor extends BukkitCommand {

    private final CommandContext commandContext;
    public CoreCommandExecutor(CommandContext commandContext) {
        super(commandContext.getCommand().name());

        this.setAliases(Arrays.stream(commandContext.getCommand().aliases()).collect(Collectors.toList()));
        this.setPermission(commandContext.getCommand().permission());

        this.commandContext = commandContext;
    }

    @FunctionalInterface
    private  interface Function<In1, Out> {
        Out apply(In1 in1);
    }


    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        RFile file = CrafttySpigotCore.getFileManager().use("messages.yml");

        CoreCommandExecutor.Function<String, Boolean> needPermission = (p) -> p != null && p.length() > 0 && !commandSender.hasPermission(p);

        if(needPermission.apply(this.commandContext.getCommand().permission())) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getString("commands.no-permission")));
        } else {
            if(this.commandContext.getSubCommands().size() == 0 || args.length == 0) {

                MethodUtils.invokeMethod(this.commandContext.getLoader().getInitalizedClass(), this.commandContext.getCommandMethod(), commandSender, s, args);
            } else {



                this.commandContext.getSubCommands().stream().filter(d -> d.getSubCommand().name().equalsIgnoreCase(args[0]) || Arrays.stream(d.getSubCommand().aliases()).anyMatch(ds -> ds.equalsIgnoreCase(args[0]))).findFirst().ifPresent(sub -> {

                    if(needPermission.apply(sub.getSubCommand().permission())) {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getString("commands.no-permission")));
                    } else {



                            MethodUtils.invokeMethod(sub.getInializedClass(),sub.getSubCommandMethod(), commandSender, s, Arrays.stream(args, 1, args.length).toArray(String[]::new));

                    }

                });

            }
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {

        if(args.length >= 1) {
            return this.commandContext.getTabCompletionContext().getSubCommandCompletions(args[0],alias, args);
        }
        return this.commandContext.getTabCompletionContext().getMainCompletions(sender,alias, args);
    }
}
