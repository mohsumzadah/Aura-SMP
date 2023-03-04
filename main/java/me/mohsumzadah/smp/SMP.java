package me.mohsumzadah.smp;

import me.mohsumzadah.smp.commands.AuraCommand;
import me.mohsumzadah.smp.commands.WithdrawCommand;
import me.mohsumzadah.smp.events.ItemsUsage.*;
import me.mohsumzadah.smp.events.OffhandInteract;
import me.mohsumzadah.smp.events.PlayerDie;
import me.mohsumzadah.smp.events.PlayerFirstJoin;
import me.mohsumzadah.smp.others.ItemMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class SMP extends JavaPlugin {

    public static ItemStack blazeAuraItem;
    public static ItemStack strengthAuraItem;
    public static ItemStack speedAuraItem;
    public static ItemStack healthAuraItem;
    public static ItemStack auraItem;
    public static ItemStack unbanItem;

    public static final HashMap<Player, ItemStack> speedPlayerItem = new HashMap<>();



    @Override
    public void onEnable() {
        createItemsFile();
        createDataFile();

        blazeAuraItem = new ItemMaker(this, "BlazeAuraItem").createItem();
        strengthAuraItem = new ItemMaker(this, "StrengthAuraItem").createItem();
        speedAuraItem = new ItemMaker(this, "SpeedAuraItem").createItem();
        healthAuraItem = new ItemMaker(this, "HealthAuraItem").createItem();
        auraItem = new ItemStack(Material.NETHER_STAR);
        unbanItem = new ItemMaker(this, "UnbanItem").createItem();

        Bukkit.addRecipe(returnBlazeAuraRecipe());
        Bukkit.addRecipe(returnAuraRecipe());
        Bukkit.addRecipe(returnHealthAuraRecipe());
        Bukkit.addRecipe(returnSpeedAuraRecipe());
        Bukkit.addRecipe(returnStrengthAuraRecipe());
        Bukkit.addRecipe(returnUnbanRecipe());

        getServer().getPluginManager().registerEvents(new PlayerFirstJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDie(this), this);
        getServer().getPluginManager().registerEvents(new AuraItemUsage(this), this);
        getServer().getPluginManager().registerEvents(new SpeedAuraItemUsage(this), this);
        getServer().getPluginManager().registerEvents(new BlazeAuraItemUsage(this), this);
        getServer().getPluginManager().registerEvents(new HealthAuraItem(this), this);
        getServer().getPluginManager().registerEvents(new StrengthAuraItem(), this);
        getServer().getPluginManager().registerEvents(new OffhandInteract(this), this);

        getCommand("withdraw").setExecutor(new WithdrawCommand(this));
        getCommand("aura").setExecutor(new AuraCommand(this));
        getCommand("aura").setTabCompleter(new AuraCommand(this));

        if (Bukkit.getOnlinePlayers().size() > 0){
            for (Player player : Bukkit.getOnlinePlayers()){
                if (data.contains(String.valueOf(player.getUniqueId()))) continue;
                data.set(String.valueOf(player.getUniqueId()), 0);
                saveDataFile();
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private Recipe returnBlazeAuraRecipe(){
        NamespacedKey key = new NamespacedKey(this, "blaze_aura_item");

        ShapedRecipe recipe = new ShapedRecipe(key, blazeAuraItem);

        recipe.shape("BOB", "OAO", "BOB");

        recipe.setIngredient('A', new RecipeChoice.ExactChoice(auraItem));
        recipe.setIngredient('B', Material.BLAZE_ROD);
        recipe.setIngredient('O', Material.ORANGE_STAINED_GLASS);

        return recipe;
    }

    private Recipe returnStrengthAuraRecipe(){
        NamespacedKey key = new NamespacedKey(this, "strength_aura_item");

        ShapedRecipe recipe = new ShapedRecipe(key, strengthAuraItem);

        recipe.shape("NRN", "RAR", "NRN");

        recipe.setIngredient('A', new RecipeChoice.ExactChoice(auraItem));
        recipe.setIngredient('N', Material.NETHER_WART);
        recipe.setIngredient('R', Material.RED_STAINED_GLASS);

        return recipe;
    }

    private Recipe returnSpeedAuraRecipe(){
        NamespacedKey key = new NamespacedKey(this, "speed_aura_item");

        ShapedRecipe recipe = new ShapedRecipe(key, speedAuraItem);

        recipe.shape("SBS", "BAB", "SBS");

        recipe.setIngredient('A', new RecipeChoice.ExactChoice(auraItem));
        recipe.setIngredient('S', Material.SUGAR);
        recipe.setIngredient('B', Material.LIGHT_BLUE_STAINED_GLASS);

        return recipe;
    }

    private Recipe returnHealthAuraRecipe(){
        NamespacedKey key = new NamespacedKey(this, "health_aura_item");

        ShapedRecipe recipe = new ShapedRecipe(key, healthAuraItem);

        recipe.shape("GLG", "LAL", "GLG");

        recipe.setIngredient('A', new RecipeChoice.ExactChoice(auraItem));
        recipe.setIngredient('G', Material.GOLDEN_APPLE);
        recipe.setIngredient('L', Material.LIME_STAINED_GLASS);

        return recipe;
    }

    private Recipe returnAuraRecipe(){
        NamespacedKey key = new NamespacedKey(this, "aura_item");

        ShapedRecipe recipe = new ShapedRecipe(key, auraItem);

        recipe.shape("NEN", "ETE", "NEN");

        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('E', Material.END_CRYSTAL);
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);

        return recipe;
    }

    private Recipe returnUnbanRecipe(){
        NamespacedKey key = new NamespacedKey(this, "unban_item");

        ShapedRecipe recipe = new ShapedRecipe(key, unbanItem);

        recipe.shape("FAF", "AIA", "FAF");

        recipe.setIngredient('A', new RecipeChoice.ExactChoice(auraItem));
        recipe.setIngredient('F', Material.AIR);
        recipe.setIngredient('I', Material.POPPED_CHORUS_FRUIT);

        return recipe;
    }


    private File items_f;
    public FileConfiguration items;

    private void createItemsFile(){
        items_f = new File(getDataFolder(), "items.yml");
        if(!items_f.exists()){
            items_f.getParentFile().mkdirs();
            saveResource("items.yml",false);
        }
        items = new YamlConfiguration();

        try {
            items.load(items_f);
        }catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    private File data_f;
    public FileConfiguration data;

    private void createDataFile(){
        data_f = new File(getDataFolder(), "data.yml");
        if(!data_f.exists()){
            data_f.getParentFile().mkdirs();
            saveResource("data.yml",false);
        }
        data = new YamlConfiguration();

        try {
            data.load(data_f);
        }catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }
    public void saveDataFile(){
        try {
            data.save(data_f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
