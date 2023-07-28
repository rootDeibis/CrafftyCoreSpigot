package me.rootdeibis.crafftty.listeners;

import me.rootdeibis.crafftty.CrafttySpigotCore;
import me.rootdeibis.crafftty.manager.Files.RFile;
import me.rootdeibis.crafftty.manager.SpawnManager;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpawnEventListener implements Listener {

    private final RFile settings = CrafttySpigotCore.getFileManager().use("settings.yml");
    private final RFile messages = CrafttySpigotCore.getFileManager().use("settings.yml");
    public SpawnEventListener(){}
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        if(!SpawnManager.isSpawnLaoded()) {
            SpawnManager.load();



            if(SpawnManager.isSpawnLaoded() && settings.getBoolean("spawn.teleport-join")){
                e.getPlayer().teleport(SpawnManager.getSpawnLocation());
            }


        }
    }

    // Spawn Protection Listener

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {

        if(settings.getBoolean("spawn.disable.interact-block")) {

            if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onMonsterSpawnEvent(EntitySpawnEvent e) {
        if(e.getEntity() instanceof Monster) {
            if(settings.getBoolean("spawn.disable.spawn-monsters")) {
                e.setCancelled(true);
            }
        }
    }
}
