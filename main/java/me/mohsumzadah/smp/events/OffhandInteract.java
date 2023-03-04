package me.mohsumzadah.smp.events;

import me.mohsumzadah.smp.SMP;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.Objects;

import static me.mohsumzadah.smp.SMP.*;

public class OffhandInteract implements Listener {

    private final SMP plugin;

    public OffhandInteract(SMP plugin){
        this.plugin = plugin;
    }

    @EventHandler
    private void blockPlace(BlockPlaceEvent event){
        if (event.getHand() == EquipmentSlot.OFF_HAND){
            ItemStack item = event.getPlayer().getInventory().getItemInOffHand().clone();
            item.setAmount(1);
            if (isAuraItem(item)){
                event.setCancelled(true);

            }

        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getClickedInventory() instanceof PlayerInventory)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = player.getInventory().getItemInOffHand().clone();
        currentItem.setAmount(1);
        int clickedSlot = event.getSlot();

        if (clickedSlot != 40) {
            return;
        }

        InventoryAction inventoryAction = event.getAction();

        if (inventoryAction == InventoryAction.PLACE_ONE || inventoryAction == InventoryAction.PLACE_SOME ||
                inventoryAction == InventoryAction.PLACE_ALL || inventoryAction == InventoryAction.SWAP_WITH_CURSOR) {
            ItemStack newOffHandItem = Objects.requireNonNull(event.getCursor()).clone();
            newOffHandItem.setAmount(1);
            if (isAuraItem(currentItem)) {
                event.setCancelled(true);
                return;
            }

            if (newOffHandItem.equals(blazeAuraItem)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            } else if (newOffHandItem.equals(strengthAuraItem)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            } else if (newOffHandItem.equals(speedAuraItem)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0, false, false));
            }
        }

        else if (inventoryAction == InventoryAction.PICKUP_ONE || inventoryAction == InventoryAction.PICKUP_SOME ||
                inventoryAction == InventoryAction.PICKUP_HALF || inventoryAction == InventoryAction.PICKUP_ALL) {
            if (currentItem.equals(blazeAuraItem)) {
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            } else if (currentItem.equals(strengthAuraItem)) {
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            } else if (currentItem.equals(speedAuraItem)) {
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
        else {
            if (isAuraItem(currentItem)) {
                event.setCancelled(true);
            }
        }


    }

    private boolean isAuraItem(ItemStack itemStack) {
        return itemStack.equals(strengthAuraItem) || itemStack.equals(speedAuraItem) ||
                itemStack.equals(blazeAuraItem) || itemStack.equals(auraItem) || itemStack.equals(healthAuraItem);
    }


    @EventHandler
    private void offhandDrag(InventoryDragEvent event) {

        if (event.getInventory().getType() == InventoryType.CRAFTING) {
            ItemStack currentItem = event.getOldCursor().clone();
            currentItem.setAmount(1);

            if (!event.getRawSlots().contains(45)) return;
            if (currentItem.equals(strengthAuraItem) ||
                    currentItem.equals(speedAuraItem) ||
                    currentItem.equals(blazeAuraItem) ||
                    currentItem.equals(auraItem) ||
                    currentItem.equals(healthAuraItem)) {

                event.setCancelled(true);
            }
        }
    }

}
