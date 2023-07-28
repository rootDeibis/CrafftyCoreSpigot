package me.rootdeibis.crafftty;

import me.rootdeibis.crafftty.commands.CrafttyCommand;
import me.rootdeibis.crafftty.commands.SpawnCommands;
import me.rootdeibis.crafftty.core.commands.CommandLoader;
import me.rootdeibis.crafftty.manager.Files.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;


public final class CrafttySpigotCore extends JavaPlugin {

    private final String settingsConfigFile = "settings.yml";;
    private final String messagesConfigFile = "messages.yml";;
    private static CrafttySpigotCore core;
    private static FileManager fileManager;
    @Override
    public void onEnable() {
        core = this;

        fileManager = new FileManager(this).
                setResourcesPath("defaults.");



        fileManager.use(this.settingsConfigFile)
                .setDefaults(this.settingsConfigFile)
                .create();
        fileManager.use(this.messagesConfigFile)
                .setDefaults(this.messagesConfigFile)
                .create();


        new CommandLoader(SpawnCommands.class).register();
        new CommandLoader(CrafttyCommand.class).register();


        loadListeners();
    }

    @Override
    public void onDisable() {
    }


    private void loadListeners() {

        Reflections reflections = new Reflections("me.rootdeibis.crafftty.listeners", new SubTypesScanner(false));

        reflections.getAllTypes().forEach(c -> {
            try {
                Class<?> listener = Class.forName(c);

                Bukkit.getPluginManager().registerEvents((Listener) listener.newInstance(), this);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
    public static CrafttySpigotCore getCore() {
        return core;
    }

    public static FileManager getFileManager() {
        return fileManager;
    }
}
