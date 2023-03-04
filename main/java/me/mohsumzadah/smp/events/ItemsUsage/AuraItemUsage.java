package me.mohsumzadah.smp.events.ItemsUsage;

import me.mohsumzadah.smp.SMP;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static me.mohsumzadah.smp.SMP.auraItem;

public class AuraItemUsage implements Listener {

    private final SMP plugin;

    public AuraItemUsage(SMP plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void rightClickItem(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand().clone();
        handItem.setAmount(1);

        if (!handItem.equals(auraItem)) {
            return;
        }

        if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        event.setCancelled(true);

        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }


        int auraPoint = plugin.data.getInt(String.valueOf(player.getUniqueId()));

        if (auraPoint >= 5) {
            player.sendMessage("You have reached the maximum capacity of aura point.");
            return;
        }

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        int heart = (int) (attribute.getBaseValue() / 2);

        if (heart < 15){
            attribute.setBaseValue((heart + 1) * 2);
        }

        plugin.data.set(String.valueOf(player.getUniqueId()), auraPoint + 1);
        plugin.saveDataFile();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getAmount() == 1) {
            player.getInventory().setItemInMainHand(null);
        } else {
            item.setAmount(item.getAmount() - 1);
        }

        player.sendMessage("1 aura point added.");


    }
}
