package me.rootdeibis.crafftty.listeners;

import me.rootdeibis.crafftty.manager.SpawnManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpawnEventListener implements Listener {

    public SpawnEventListener() {

    }
    @EventHandler
    public void worldLoadEvent(PlayerJoinEvent e) {
        if(!SpawnManager.isSpawnLaoded()) {
            SpawnManager.load();

            if(SpawnManager.isSpawnLaoded()){
                e.getPlayer().teleport(SpawnManager.getSpawnLocation());
            }


        }
    }
}
