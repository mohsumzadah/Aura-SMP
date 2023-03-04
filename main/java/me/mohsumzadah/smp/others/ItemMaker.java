package me.mohsumzadah.smp.others;

import me.mohsumzadah.smp.SMP;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemMaker {

    private final Material type;
    private final String name;
    private final List<String> lores;

    public ItemMaker(SMP plugin, String dataPath) {
        ConfigurationSection section = plugin.items.getConfigurationSection(dataPath);


        this.type = Material.getMaterial(section.getString("type"));
        this.name = new ColorTranslate(section.getString("name")).translateColor();
        this.lores = section.getStringList("lores");
    }

    public ItemStack createItem(){
        ItemStack item = new ItemStack(type);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(name);

        List<String> translatedLores = new ArrayList<>();
        for (String lore : lores) {
            translatedLores.add(new ColorTranslate(lore).translateColor());
        }
        itemMeta.setLore(translatedLores);

        itemMeta.addEnchant(Enchantment.BINDING_CURSE, 1, false);

        item.setItemMeta(itemMeta);

        return item;
    }
}
