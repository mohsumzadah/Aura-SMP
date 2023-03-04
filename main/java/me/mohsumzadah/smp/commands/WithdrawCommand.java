package me.mohsumzadah.smp.commands;

import me.mohsumzadah.smp.SMP;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.mohsumzadah.smp.SMP.auraItem;

public class WithdrawCommand implements CommandExecutor {

    private final SMP plugin;

    public WithdrawCommand(SMP plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = ((Player) sender).getPlayer();
            assert player != null;


            int auraPoint = plugin.data.getInt(String.valueOf(player.getUniqueId()));

            if (args.length == 0) {
                if (auraPoint > -4) {
                    player.getInventory().addItem(auraItem.clone());

                    plugin.data.set(String.valueOf(player.getUniqueId()), auraPoint - 1);
                    plugin.saveDataFile();
                } else {
                    player.sendMessage("You don't have enough aura point.");
                }
            } else if (args.length == 1 && args[0].matches("-?\\d+")) {
                int withdrawAuraPoint = Integer.parseInt(args[0]);

                if (withdrawAuraPoint > 0 && auraPoint - withdrawAuraPoint > -5) {
                    ItemStack newAuraItem = auraItem.clone();
                    newAuraItem.setAmount(withdrawAuraPoint);
                    player.getInventory().addItem(newAuraItem);
                    plugin.data.set(String.valueOf(player.getUniqueId()), auraPoint - withdrawAuraPoint);
                    plugin.saveDataFile();
                } else {
                    player.sendMessage("You don't have enough aura point.");
                }
            }
        }
        return false;
    }
}
