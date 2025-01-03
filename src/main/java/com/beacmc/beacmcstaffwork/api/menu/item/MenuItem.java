package com.beacmc.beacmcstaffwork.api.menu.item;

import com.beacmc.beacmcstaffwork.util.Message;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public class MenuItem {

    protected final ItemStack item;

    private final Material material;
    private int slot;
    private String name;
    private List<String> lore;
    private List<String> actions;


    public MenuItem(Material material) {
        this.material = material != null ? material : Material.DIRT;
        this.item = new ItemStack(material);
    }

    public MenuItem setActions(List<String> actions) {
        this.actions = actions;
        return this;
    }

    public int getSlot() {
        return slot;
    }

    public MenuItem setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public MenuItem setLore(List<String> lore) {
        if (item.hasItemMeta()) {
            lore = lore.stream().map(Message::of).toList();

            this.lore = lore;
            ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return this;
    }

    public MenuItem setName(String name) {
        if (item.hasItemMeta()) {
            name = Message.of(name);

            this.name = name;
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return this;
    }

    public List<String> getActions() {
        return actions;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public ItemStack getRealItem() {
        return item;
    }
}
