package com.beacmc.beacmcstaffwork.api.menu.listener;


import java.util.HashMap;
import java.util.Map;

import com.beacmc.beacmcstaffwork.api.menu.Panel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuListener implements Listener {
    private static final Map<JavaPlugin, Listener> listenerMap = new HashMap<>();

    public static void register(JavaPlugin plugin) {
        if (!listenerMap.containsKey(plugin)) {
            MenuListener panelListener = new MenuListener();
            listenerMap.put(plugin, panelListener);
            Bukkit.getPluginManager().registerEvents(panelListener, plugin);
        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            Inventory inventory = event.getClickedInventory();
            if (inventory.getHolder() instanceof Panel panel) {
                event.setCancelled(true);
                panel.handleClick(event);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof Panel panel) {
            panel.handleClose(event);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof Panel panel) {
            panel.handleDrag(event);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof Panel panel) {
            panel.handleOpen(event);
        }
    }
}
