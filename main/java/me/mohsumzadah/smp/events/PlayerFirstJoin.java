package me.mohsumzadah.smp.events;

import me.mohsumzadah.smp.SMP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerFirstJoin implements Listener {

    private final SMP plugin;

    public PlayerFirstJoin(SMP plugin){
        this.plugin = plugin;
    }

    @EventHandler
    private void playerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if (!plugin.data.contains(String.valueOf(player.getUniqueId()))){
            plugin.data.set(String.valueOf(player.getUniqueId()), 0);
            plugin.saveDataFile();
        }
    }
}
