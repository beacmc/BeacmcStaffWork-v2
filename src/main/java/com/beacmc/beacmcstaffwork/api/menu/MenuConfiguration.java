package com.beacmc.beacmcstaffwork.api.menu;

import com.beacmc.beacmcstaffwork.api.menu.item.MenuItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LightningStrike;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class MenuConfiguration {

    protected final File file;
    protected YamlConfiguration config;
    protected List<MenuItem> menuItems;

    public MenuConfiguration(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public String getTitle() {
        return config.getString("title");
    }

    public int getSize() {
        return config.getInt("size");
    }

    public void loadMenuItems() {
        final ConfigurationSection itemsSection = config.getConfigurationSection("items");

        for (@NotNull String item : itemsSection.getKeys(false)) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(item);
            Material material = Material.getMaterial(itemSection.getString("material", "DIRT"));

            MenuItem menuItem = new MenuItem(material)
                    .setName(itemSection.getString("display_name"))
                    .setLore(itemSection.getStringList("lore"))
                    .setActions(itemSection.getStringList("actions"));

            menuItems.add(menuItem);
        }
    }

    public MenuItem getMenuItemByItemStack(ItemStack stack) {
        return menuItems.stream()
                .filter(i -> i.getRealItem().equals(stack))
                .findFirst()
                .orElse(null);
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }
}
