package com.beacmc.beacmcstaffwork;

import com.beacmc.beacmcstaffwork.commands.StaffAdminCommand;
import com.beacmc.beacmcstaffwork.commands.StaffChat;
import com.beacmc.beacmcstaffwork.commands.StaffCommand;
import com.beacmc.beacmcstaffwork.data.Placeholder;
import com.beacmc.beacmcstaffwork.data.sql.SQLBuilder;
import com.beacmc.beacmcstaffwork.data.sql.SQLManager;
import com.beacmc.beacmcstaffwork.listener.ABListener;
import com.beacmc.beacmcstaffwork.listener.MainListener;
import com.beacmc.beacmcstaffwork.manager.Color;
import com.beacmc.beacmcstaffwork.manager.Config;
import com.beacmc.beacmcstaffwork.manager.LiteBansHandler;
import com.beacmc.beacmcstaffwork.manager.UpdateChecker;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public final class BeacmcStaffWork extends JavaPlugin {



    private static LuckPerms luckPerms;
    public HashSet<UUID> staff = new HashSet<>();
    private static BeacmcStaffWork instance;



    @Override
    public void onEnable() {



        instance = this;
        if (Config.getBoolean("settings.update-check")) {


            String latest = UpdateChecker.start();
            if (!latest.equals(this.getDescription().getVersion())) {
                ArrayList<String> list = new ArrayList<>(Config.getStringList("settings.messages.update-check-console"));
                for (String execute : list) {
                    System.out.println(Color.compile(execute).replace("{current_version}", this.getDescription().getVersion()).replace("{latest_version}", latest));
                }
            }
        }

        this.luckPerms = this.getServer().getServicesManager().load(LuckPerms.class);
        new StaffCommand(this.luckPerms);
        new StaffAdminCommand();
        new StaffChat();




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


    public static BeacmcStaffWork getInstance() {
        return instance;
    }

    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
