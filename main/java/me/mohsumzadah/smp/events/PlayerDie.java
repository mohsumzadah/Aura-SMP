package me.mohsumzadah.smp.events;

import me.mohsumzadah.smp.SMP;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


public class PlayerDie implements Listener {

    private final SMP plugin;

    public PlayerDie(SMP plugin){
        this.plugin = plugin;
    }

    @EventHandler
    private void playerDead(PlayerDeathEvent event){
        Player victim = event.getEntity().getPlayer();

        int victimAura = plugin.data.getInt(String.valueOf(victim.getUniqueId())) - 1;

        if (victimAura < 0) {
            AttributeInstance attribute = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH);

            if (victimAura == -5){
                plugin.data.set(String.valueOf(victim.getUniqueId()), 0);
                plugin.saveDataFile();
                attribute.setBaseValue(20);

                BanList banList = Bukkit.getBanList(BanList.Type.NAME);
                banList.addBan(victim.getName(), ChatColor.WHITE + "You are banned from server because you have '-5' aura point.",
                        null, "Console");
                victim.kickPlayer(ChatColor.WHITE + "You are banned from server because you have '-5' aura point.");

            }else {
                int heart = (int) (attribute.getBaseValue() / 2);
                if (heart != 5){
                    attribute.setBaseValue((heart - 1) * 2);
                }

                plugin.data.set(String.valueOf(victim.getUniqueId()), victimAura);
                plugin.saveDataFile();
            }


        } else {
            plugin.data.set(String.valueOf(victim.getUniqueId()), victimAura);
            plugin.saveDataFile();
        }

        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        int killerAura = plugin.data.getInt(String.valueOf(killer.getUniqueId())) + 1;

        if (killerAura > 0){
            AttributeInstance attribute = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            int heart = (int) (attribute.getBaseValue() / 2);

            if (heart != 15){
                attribute.setBaseValue((heart + 1) * 2);
            }
        }
        if (killerAura < 5) {
            plugin.data.set(String.valueOf(killer.getUniqueId()), killerAura);
        }



    }
}
