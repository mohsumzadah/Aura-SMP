package me.mohsumzadah.smp.others;

import org.bukkit.ChatColor;

public class ColorTranslate {
    private final String text;

    public ColorTranslate(String text) {
        this.text = text;
    }

    public String translateColor() {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
