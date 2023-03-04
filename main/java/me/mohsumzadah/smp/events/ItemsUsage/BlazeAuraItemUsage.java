package me.mohsumzadah.smp.events.ItemsUsage;

import me.mohsumzadah.smp.SMP;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.mohsumzadah.smp.SMP.blazeAuraItem;

public class BlazeAuraItemUsage implements Listener {

    private Map<Player, Long> lastResistanceTime = new HashMap<>();
    private SMP plugin;
    private List<Player> onWaterPlayers;

    public BlazeAuraItemUsage(SMP plugin){
        this.plugin = plugin;
        onWaterPlayers = new ArrayList<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (onWaterPlayers.size() > 0){
                    for (Player player : onWaterPlayers){
                        player.setRemainingAir(0);
                        player.damage(1);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    @EventHandler
    public void onPlayerHoldItem(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getOffHandItem();

        if (item != null && item.equals(blazeAuraItem)) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0,
                            false, false));
                }
            }.runTaskLater(plugin, 2);

        } else {
            onWaterPlayers.remove(player);
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player killer = (Player) event.getDamager();

        if (event.getDamage() > 0) {
            if (killer.getInventory().getItemInOffHand().equals(blazeAuraItem)) {
                Player victim = (Player) event.getEntity();

                long currentTime = System.currentTimeMillis();
                long lastTime = lastResistanceTime.getOrDefault(killer, 0L);
                long timeSinceLast = currentTime - lastTime;
                if (timeSinceLast >= 1000 * 60 * 3) {
                    victim.setVisualFire(true);
                    final int[] second = {0};

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (second[0] != 6) {
                                second[0] += 1;

                                ItemStack[] armor = victim.getEquipment().getArmorContents();

                                boolean hasProtection4 = true;
                                for (ItemStack item : armor) {
                                    if (item != null && item.getType() != Material.AIR) {
                                        ItemMeta meta = item.getItemMeta();
                                        if (meta.hasEnchant(Enchantment.PROTECTION_FIRE) && meta.getEnchantLevel(Enchantment.PROTECTION_FIRE) == 4) {
                                            continue;
                                        }
                                    }
                                    hasProtection4 = false;
                                    break;
                                }

                                if (hasProtection4) {
                                    victim.damage(1);

                                }else {
                                    victim.damage(2);

                                }

                            }else {
                                this.cancel();
                                victim.setVisualFire(false);
                            }


                        }
                    }.runTaskTimer(plugin, 0, 20);
                    lastResistanceTime.put(killer, currentTime);
                }

            }
        }
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
        if (itemInOffHand.equals(blazeAuraItem)) {
            if(player.getLocation().getBlock().getType() == Material.WATER) {
                if (!onWaterPlayers.contains(player)){
                    onWaterPlayers.add(player);

                }
            }else {
                if (onWaterPlayers.contains(player)){
                    onWaterPlayers.remove(player);
                }
            }
        }
    }
}
