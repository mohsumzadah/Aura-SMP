package me.mohsumzadah.smp.events.ItemsUsage;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

import static me.mohsumzadah.smp.SMP.*;

public class StrengthAuraItem implements Listener {

    @EventHandler
    public void onPlayerHoldItem(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = event.getOffHandItem();
        if (heldItem != null && heldItem.equals(strengthAuraItem)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
    }

    private final HashMap<Player, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
//        if (! (event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        if (! (event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        if (!offHandItem.equals(strengthAuraItem)) {
            return; // player not holding the healthAuraItem in offhand
        }
        if (event.getFinalDamage() > 0) {
            long cooldown = cooldowns.getOrDefault(player, 0L);
            if (System.currentTimeMillis() - cooldown >= 60 * 1000) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 0));
                cooldowns.put(player, System.currentTimeMillis());
            }
        }

    }

    @EventHandler
    public void onPlayerDamageByMob(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (player.getInventory().getItemInOffHand().equals(strengthAuraItem)) {
            if (event.getDamager() instanceof Monster) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 6, 0, false, false));
            }
        }
    }
}
