package me.mohsumzadah.smp.events.ItemsUsage;

import me.mohsumzadah.smp.SMP;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static me.mohsumzadah.smp.SMP.speedAuraItem;
import static me.mohsumzadah.smp.SMP.speedPlayerItem;

public class SpeedAuraItemUsage implements Listener {

    private SMP plugin;

    public SpeedAuraItemUsage(SMP plugin){

        this.plugin = plugin;

    }

    @EventHandler
    public void onPlayerHoldItem(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack offhandItem = event.getOffHandItem().clone();
        offhandItem.setAmount(1);
        ItemStack mainHandItem = event.getMainHandItem();

        if (offhandItem != null && offhandItem.equals(speedAuraItem)) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0,
                            false, false));
                }
            }.runTaskLater(plugin, 2);


        } else {
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);

            if (speedPlayerItem.get(player) != null){
                ItemStack old_item = speedPlayerItem.get(player);
                ItemMeta old_item_meta = old_item.getItemMeta();
                old_item_meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
                old_item.setItemMeta(old_item_meta);
                speedPlayerItem.remove(player);
            }
        }

    }




    @EventHandler
    public void onPlayerItemHeld(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        ItemStack offhandItem = player.getInventory().getItemInOffHand().clone();
        offhandItem.setAmount(1);
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();

        if (mainHandItem.getType().name().endsWith("_SWORD") || mainHandItem.getType().name().endsWith("_AXE")) {
            if (speedPlayerItem.get(player) != null && speedPlayerItem.get(player).equals(mainHandItem)) return;
            if (speedPlayerItem.get(player) != null){
                if (!speedPlayerItem.get(player).equals(mainHandItem)) {
                    ItemStack old_item = speedPlayerItem.get(player);
                    ItemMeta old_item_meta = old_item.getItemMeta();
                    old_item_meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
                    old_item.setItemMeta(old_item_meta);
                    speedPlayerItem.remove(player);
                }
            }

            ItemMeta itemMeta = mainHandItem.getItemMeta();
            if (!itemMeta.hasAttributeModifiers()) {
                if (offhandItem.equals(speedAuraItem)) {
                    AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 0.1, AttributeModifier.Operation.ADD_NUMBER);
                    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
                    mainHandItem.setItemMeta(itemMeta);
                    speedPlayerItem.put(player, mainHandItem);
                }
            }else {
                if (!offhandItem.equals(speedAuraItem)) {
                    itemMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
                    mainHandItem.setItemMeta(itemMeta);

                }
            }
        }
        else{
            if (speedPlayerItem.get(player) != null){
                    ItemStack old_item = speedPlayerItem.get(player);
                    ItemMeta old_item_meta = old_item.getItemMeta();
                    old_item_meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
                    old_item.setItemMeta(old_item_meta);
                    speedPlayerItem.remove(player);

            }
        }
    }



    private final HashMap<Player, Integer> sneakTimes = new HashMap<>();
    List<Player> sneakPlayers = new ArrayList<>();

    @EventHandler
    public void onPlayerToggleSneak(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = event.getPlayer().getInventory().getItemInOffHand();


        if (heldItem != null && heldItem.equals(speedAuraItem)) {
            if (player.isSneaking()) {
                if (!sneakPlayers.contains(player)) {
                    sneakPlayers.add(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.isSneaking()) {
                                int totalSecond = 0;
                                if (sneakTimes.containsKey(player)) {
                                    totalSecond = sneakTimes.get(player);
                                }

                                if (totalSecond >= 60) {
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5 * 20, 1));
                                    sneakTimes.remove(player);
                                } else {
                                    sneakTimes.put(player, totalSecond + 1);
                                }

                            } else {
                                sneakPlayers.remove(player);
                                this.cancel();
                            }

                        }
                    }.runTaskTimer(plugin, 0, 20);
                }
            }
        }
    }

}
