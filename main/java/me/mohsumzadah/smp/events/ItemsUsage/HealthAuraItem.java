package me.mohsumzadah.smp.events.ItemsUsage;

import me.mohsumzadah.smp.SMP;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

import static me.mohsumzadah.smp.SMP.healthAuraItem;

public class HealthAuraItem implements Listener {

    private HashMap<Player, Long> cooldowns = new HashMap<>();
    private SMP plugin;

    public HealthAuraItem(SMP plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            ItemStack offHandItem = player.getInventory().getItemInOffHand();

            if (!offHandItem.equals(healthAuraItem)) {
                return; // player not holding the healthAuraItem in offhand
            }

            if (event.getFinalDamage() > 0) {
                long cooldown = cooldowns.getOrDefault(player, 0L);
                if (System.currentTimeMillis() - cooldown >= 60000) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 0));
                    cooldowns.put(player, System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler
    public void playerRun(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        if (offHandItem.equals(healthAuraItem) && player.getHealth() < 4) {
            if (player.isSprinting()){
                player.teleport(event.getFrom());
                player.sendMessage(ChatColor.RED + "You cannot sprint when your health is below 2 hearts!");
            }
        }
    }



    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        ItemStack offHandItem = player.getInventory().getItemInOffHand();


        if (!offHandItem.equals(healthAuraItem)) {
            return; // player not holding the healthAuraItem in offhand
        }

        if (item.getType() == Material.GOLDEN_APPLE || item.getType() == Material.ENCHANTED_GOLDEN_APPLE) {
            return; // ignore golden apples
        }

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) {

            // Add an extra attribute modifier for the yellow heart

            if (player.getAbsorptionAmount() < 10){
                player.setAbsorptionAmount(player.getAbsorptionAmount()+2);

            }

        }
    }


}
