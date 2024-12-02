package com.beacmc.beacmcstaffwork.menu;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.menu.annotation.Click;
import com.beacmc.beacmcstaffwork.api.menu.container.Panel;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Color;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StaffPanel extends Panel {

    private final List<StaffPlayer> workedPlayers;
    private final List<Integer> playerSlots;
    private final int itemsPerPage;
    private int currentPage;

    public StaffPanel() {
        super(BeacmcStaffWork.getInstance());
        this.currentPage = 0;
        this.playerSlots = Config.getIntegerList("settings.menu.items.staff-player.slots");
        this.workedPlayers = new ArrayList<>(BeacmcStaffWork.getUsers());
        this.itemsPerPage = playerSlots.size();
    }

    @Override
    public int getSize() {
        return Config.getInteger("settings.menu.size");
    }

    @Override
    public String getTitle() {
        return Color.compile(Config.getString("settings.menu.title"));
    }

    @Override
    public void setItems() {
        inventory.clear();

        List<StaffPlayer> pagePlayers = getPagePlayers();
        for (int i = 0; i < pagePlayers.size(); i++) {
            StaffPlayer player = pagePlayers.get(i);
            ItemStack skull = createPlayerSkull(player);
            inventory.setItem(playerSlots.get(i), skull);
        }

        if (currentPage > 0) {
            inventory.setItem(Config.getInteger("settings.menu.items.previous-page.slot"), createNavigationItem(
                    Material.valueOf(Config.getString("settings.menu.items.previous-page.material")),
                    Config.getString("settings.menu.items.previous-page.name"),
                    Config.getStringList("settings.menu.items.previous-page.lore")
            ));
        }

        if (currentPage < getTotalPages() - 1) {
            inventory.setItem(Config.getInteger("settings.menu.items.next-page.slot"), createNavigationItem(
                    Material.valueOf(Config.getString("settings.menu.items.next-page.material")),
                    Config.getString("settings.menu.items.next-page.name"),
                    Config.getStringList("settings.menu.items.next-page.lore")
            ));
        }
    }

    @Click
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        int slot = event.getSlot();

        if (slot == Config.getInteger("settings.menu.items.previous-page.slot") && currentPage > 0) {
            currentPage--;
            update();
        } else if (slot == Config.getInteger("settings.menu.items.next-page.slot") && currentPage < getTotalPages() - 1) {
            currentPage++;
            update();
        }
    }

    private ItemStack createPlayerSkull(StaffPlayer staffPlayer) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(staffPlayer.getPlayer());
            meta.setDisplayName(formattedMessage(staffPlayer.getPlayer(), Config.getString("settings.menu.items.staff-player.name")));
            meta.setLore(Config.getStringList("settings.menu.items.staff-player.lore").stream()
                    .map(lore -> formattedMessage(staffPlayer.getPlayer(), lore))
                    .collect(Collectors.toList()));
        }
        skull.setItemMeta(meta);
        return skull;
    }

    private ItemStack createNavigationItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Color.compile(name));
            meta.setLore(lore.stream().map(Color::compile).collect(Collectors.toList()));
        }
        item.setItemMeta(meta);
        return item;
    }

    private String formattedMessage(Player player, String message) {
        return Color.compile(PlaceholderAPI.setPlaceholders(player, message));
    }

    private List<StaffPlayer> getPagePlayers() {
        int start = currentPage * itemsPerPage;
        int end = Math.min(start + itemsPerPage, workedPlayers.size());
        return workedPlayers.subList(start, end);
    }

    private int getTotalPages() {
        return (int) Math.ceil((double) workedPlayers.size() / itemsPerPage);
    }
}
