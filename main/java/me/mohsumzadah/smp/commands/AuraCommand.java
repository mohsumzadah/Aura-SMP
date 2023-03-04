package me.mohsumzadah.smp.commands;

import me.mohsumzadah.smp.SMP;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static me.mohsumzadah.smp.SMP.unbanItem;

public class AuraCommand implements CommandExecutor, TabCompleter {

    private final SMP plugin;
    public AuraCommand(SMP plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = ((Player) sender).getPlayer();
            assert player != null;

            if (args.length == 0) {
                int auraPoint = plugin.data.getInt(String.valueOf(player.getUniqueId()));

                player.sendMessage(ChatColor.WHITE + "You have '" + auraPoint + "' aura point");

            } else if (args.length == 2){
                if (args[0].equals("unban")) {
                    ItemStack item = player.getInventory().getItemInMainHand().clone();
                    item.setAmount(1);
                    if (item.equals(unbanItem)) {
                        if (Bukkit.getBanList(BanList.Type.NAME).isBanned(args[1])) {


                            ItemStack unbanItem = player.getInventory().getItemInMainHand();

                            if (unbanItem.getAmount() == 1) {
                                player.getInventory().setItemInMainHand(null);
                            } else {
                                unbanItem.setAmount(unbanItem.getAmount() - 1);
                            }

                            Bukkit.getBanList(BanList.Type.NAME).pardon(args[1]);
                            player.sendMessage(ChatColor.GOLD + args[1] + ChatColor.WHITE + " unbanned.");
                        } else {
                            player.sendMessage(ChatColor.GOLD + args[1] + ChatColor.WHITE + " don't have ban.");
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "You ara not holding an unban item.");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 2) {
                if (args[0].equals("unban")) {
                    ItemStack item = player.getInventory().getItemInMainHand().clone();
                    item.setAmount(1);
                    if (item.equals(unbanItem)) {
                        List<String> bannedPlayers = new ArrayList<>();
                        for (BanEntry entry : Bukkit.getBanList(BanList.Type.NAME).getBanEntries()) {
                            bannedPlayers.add(entry.getTarget());
                        }
                        return bannedPlayers;
                    }
                }
            }

        }
        return null;
    }
}
