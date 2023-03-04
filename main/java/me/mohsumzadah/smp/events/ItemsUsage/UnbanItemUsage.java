package me.mohsumzadah.smp.events.ItemsUsage;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.mohsumzadah.smp.SMP.unbanItem;

public class UnbanItemUsage implements Listener {

    @EventHandler
    private void playerInteract(PlayerInteractEvent event){
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) ||
                event.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if (event.getItem().equals(unbanItem)){
                event.setCancelled(true);
            }
        }
    }
}
