package com.beacmc.beacmcstaffwork;

import com.beacmc.beacmcstaffwork.commands.StaffAdminCommand;
import com.beacmc.beacmcstaffwork.commands.StaffChatCommand;
import com.beacmc.beacmcstaffwork.commands.StaffCommand;
import com.beacmc.beacmcstaffwork.commands.tabcompleter.StaffAdminCompleter;
import com.beacmc.beacmcstaffwork.commands.tabcompleter.StaffCompleter;
import com.beacmc.beacmcstaffwork.data.Placeholder;
import com.beacmc.beacmcstaffwork.data.sql.SQLBuilder;
import com.beacmc.beacmcstaffwork.data.sql.SQLManager;
import com.beacmc.beacmcstaffwork.listener.ABListener;
import com.beacmc.beacmcstaffwork.listener.MainListener;
import com.beacmc.beacmcstaffwork.manager.Color;
import com.beacmc.beacmcstaffwork.manager.CooldownManager;
import com.beacmc.beacmcstaffwork.manager.LiteBansHandler;
import com.beacmc.beacmcstaffwork.manager.UpdateChecker;
import com.beacmc.beacmcstaffwork.manager.configuration.Config;
import com.beacmc.beacmcstaffwork.util.Runner;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class BeacmcStaffWork extends JavaPlugin {



    private static LuckPerms luckPerms;
    private static BeacmcStaffWork instance;

    public static Map<String, CooldownManager> cooldowns = new HashMap<>();

    @Override
    public void onEnable() {

        instance = this;

        update();

        this.luckPerms = this.getServer().getServicesManager().load(LuckPerms.class);
        new StaffCommand(this.luckPerms);
        new StaffAdminCommand();
        new StaffChatCommand();


        this.getCommand("staffwork").setTabCompleter(new StaffCompleter());
        this.getCommand("staffworkadmin").setTabCompleter(new StaffAdminCompleter());

        this.saveDefaultConfig();

        if(this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholder().load();

        } else {
            this.getLogger().severe("Установите плагин PlaceholderAPI для работы плагина");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        if(!this.getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            this.getLogger().severe("Установите плагин LuckPerms для работы плагина");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        if(isLiteBansEnabled() && !isAdvancedBanEnabled()) {
            new LiteBansHandler().register();
        }

        if(isAdvancedBanEnabled() && !isLiteBansEnabled()) {
            this.getServer().getPluginManager().registerEvents(new ABListener(), this);
        }

        this.getServer().getPluginManager().registerEvents(new MainListener(), this);
        new SQLBuilder();

        Runner.run();
    }

    private boolean isLiteBansEnabled() {
        return this.getServer().getPluginManager().isPluginEnabled("LiteBans");
    }

    private boolean isAdvancedBanEnabled() {
        return this.getServer().getPluginManager().isPluginEnabled("AdvancedBan");
    }

    @Override
    public void onDisable() {
        SQLManager.close();
        instance = null;
    }


    private void update() {
        if (!this.getConfig().getBoolean("settings.update-check"))
            return;

        String latest = UpdateChecker.start();
        if (!latest.equals(this.getDescription().getVersion())) {
            ArrayList<String> list = new ArrayList<>(Config.getStringList("settings.messages.update-check-console"));
            list.forEach(execute -> {
                System.out.println(Color.compile(execute).replace("{current_version}", this.getDescription().getVersion()).replace("{latest_version}", latest));
            });
        }
    }

    public static BeacmcStaffWork getInstance() {
        return instance;
    }

    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
