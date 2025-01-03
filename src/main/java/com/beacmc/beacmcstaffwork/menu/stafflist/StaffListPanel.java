package com.beacmc.beacmcstaffwork.menu.stafflist;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.api.menu.annotation.Click;
import com.beacmc.beacmcstaffwork.api.menu.Panel;
import com.beacmc.beacmcstaffwork.config.MainConfiguration;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.util.Message;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StaffListPanel extends Panel {

    private final List<Integer> playerSlots;
    private final int itemsPerPage;
    private Set<StaffPlayer> workedPlayers;
    private int currentPage;

    public StaffListPanel() {
        super(BeacmcStaffWork.getInstance());
        final ConfigurationSection staffListSettings = BeacmcStaffWork.getMainConfig().getStaffPanelSettings();
        this.currentPage = 0;
        this.playerSlots = staffListSettings.getIntegerList("items.staff-player.slots");
        this.itemsPerPage = playerSlots.size();
    }

    @Override
    public int getSize() {
        final ConfigurationSection staffListSettings = BeacmcStaffWork.getMainConfig().getStaffPanelSettings();
        return staffListSettings.getInt("size");
    }

    @Override
    public String getTitle() {
        return Message.fromConfig("settings.menu.title");
    }

    @Override
    public void setItems() {
        final MainConfiguration config = BeacmcStaffWork.getMainConfig();
        final ConfigurationSection staffListSettings = config.getStaffPanelSettings();

        this.workedPlayers = BeacmcStaffWork.getStaffWorkManager().getWorkedPlayers();
        inventory.clear();

        List<StaffPlayer> pagePlayers = getPagePlayers();
        for (int i = 0; i < pagePlayers.size(); i++) {
            StaffPlayer player = pagePlayers.get(i);
            ItemStack skull = createPlayerSkull(player);
            inventory.setItem(playerSlots.get(i), skull);
        }

        if (currentPage > 0) {
            inventory.setItem(staffListSettings.getInt("items.previous-page.slot"), createNavigationItem(
                    Material.valueOf(staffListSettings.getString("items.previous-page.material")),
                    staffListSettings.getString("items.previous-page.name"),
                    staffListSettings.getStringList("items.previous-page.lore")
            ));
        }

        if (currentPage < getTotalPages() - 1) {
            inventory.setItem(staffListSettings.getInt("items.next-page.slot"), createNavigationItem(
                    Material.valueOf(staffListSettings.getString("items.next-page.material")),
                    staffListSettings.getString("items.next-page.name"),
                    staffListSettings.getStringList("items.next-page.lore")
            ));
        }
    }

    @Click
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        final int slot = event.getSlot();
        final MainConfiguration config = BeacmcStaffWork.getMainConfig();

        if (slot == config.getStaffPanelSettings().getInt("items.previous-page.slot") && currentPage > 0) {
            currentPage--;
            update();
        } else if (slot == config.getStaffPanelSettings().getInt("items.next-page.slot") && currentPage < getTotalPages() - 1) {
            currentPage++;
            update();
        }
    }

    private ItemStack createPlayerSkull(StaffPlayer staffPlayer) {
        final MainConfiguration config = BeacmcStaffWork.getMainConfig();

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(staffPlayer.getPlayer());
            meta.setDisplayName(formattedMessage(staffPlayer.getPlayer(), config.getStaffPanelSettings().getString("items.staff-player.name")));
            meta.setLore(config.getStaffPanelSettings().getStringList("items.staff-player.lore").stream()
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
        return workedPlayers.stream().toList().subList(start, end);
    }

    private int getTotalPages() {
        return (int) Math.ceil((double) workedPlayers.size() / itemsPerPage);
    }
}
