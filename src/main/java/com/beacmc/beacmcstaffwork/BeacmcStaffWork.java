package com.beacmc.beacmcstaffwork;

import com.beacmc.beacmcstaffwork.action.BaseActionManager;
import com.beacmc.beacmcstaffwork.action.creator.*;
import com.beacmc.beacmcstaffwork.api.addon.AddonManager;
import com.beacmc.beacmcstaffwork.command.staffmenu.StaffMenuCommand;
import com.beacmc.beacmcstaffwork.config.MainConfiguration;
import com.beacmc.beacmcstaffwork.listener.WorkListener;
import com.beacmc.beacmcstaffwork.api.action.ActionManager;
import com.beacmc.beacmcstaffwork.command.admin.StaffAdminCommand;
import com.beacmc.beacmcstaffwork.command.staffchat.StaffChatCommand;
import com.beacmc.beacmcstaffwork.command.player.StaffCommand;
import com.beacmc.beacmcstaffwork.command.admin.tabcompleter.StaffAdminCompleter;
import com.beacmc.beacmcstaffwork.command.player.tabcompleter.StaffCompleter;
import com.beacmc.beacmcstaffwork.hook.placeholderapi.PlaceholderHook;
import com.beacmc.beacmcstaffwork.database.Database;
import com.beacmc.beacmcstaffwork.discord.DiscordBot;
import com.beacmc.beacmcstaffwork.lib.LibraryManager;
import com.beacmc.beacmcstaffwork.hook.advancedban.BanListener;
import com.beacmc.beacmcstaffwork.listener.CommandListener;
import com.beacmc.beacmcstaffwork.listener.MainListener;
import com.beacmc.beacmcstaffwork.listener.PluginListener;
import com.beacmc.beacmcstaffwork.util.metrics.MetricBuilder;
import com.beacmc.beacmcstaffwork.work.StaffWorkManager;
import com.beacmc.beacmcstaffwork.hook.litebans.LiteBansHandler;
import com.beacmc.beacmcstaffwork.util.UpdateChecker;
import com.beacmc.beacmcstaffwork.listener.MessagingListener;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.io.File;

public final class BeacmcStaffWork extends JavaPlugin {

    private static LuckPerms luckPerms;
    private static DiscordBot discordBot;
    private static BeacmcStaffWork instance;
    private static Database database;
    private static ActionManager actionManager;
    private static StaffWorkManager staffWorkManager;
    private static MainConfiguration mainConfig;
    private static AddonManager addonManager;


    @Override
    public void onEnable() {
        if(!this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.getLogger().severe("Установите PlaceholderAPI для работы плагина");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        if(!this.getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            this.getLogger().severe("Установите LuckPerms для работы плагина");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        this.reloadConfig();

        new LibraryManager(this);
        Messenger messenger = this.getServer().getMessenger();

        instance = this;
        database = new Database();
        database.connect();

        actionManager = new BaseActionManager();
        actionManager.registerActions(new ConsoleAction(), new PlayerAction(), new SoundAction(), new MessageToModeratosAction(), new MessageAction(), new BroadcastAction(), new ActionbarAction());

        staffWorkManager = new StaffWorkManager();
        UpdateChecker.startCheck();

        new PlaceholderHook().load();
        luckPerms = this.getServer().getServicesManager().load(LuckPerms.class);

        new StaffCommand();
        new StaffAdminCommand();
        new StaffChatCommand();
        new StaffMenuCommand();
        if (mainConfig.getDiscordSettings().getBoolean("enable")) {
            discordBot = new DiscordBot();
            discordBot.connect();
        }

        this.getCommand("staffwork").setTabCompleter(new StaffCompleter());
        this.getCommand("staffworkadmin").setTabCompleter(new StaffAdminCompleter());

        if (mainConfig.getSettings().getBoolean("proxy")) {
            messenger.registerIncomingPluginChannel(this, "BungeeCord", new MessagingListener());
            messenger.registerOutgoingPluginChannel(this, "BungeeCord");
        }

        if(isLiteBansEnabled() && !isAdvancedBanEnabled()) {
            new LiteBansHandler().register();
        }

        if(isAdvancedBanEnabled() && !isLiteBansEnabled()) {
            this.getServer().getPluginManager().registerEvents(new BanListener(), this);
        }

        addonManager = new AddonManager();
        addonManager.loadAddons();

        this.getServer().getPluginManager().registerEvents(new MainListener(), this);
        this.getServer().getPluginManager().registerEvents(new WorkListener(), this);
        this.getServer().getPluginManager().registerEvents(new PluginListener(), this);
        this.getServer().getPluginManager().registerEvents(new CommandListener(), this);

        new MetricBuilder().build(24125);
    }

    private boolean isLiteBansEnabled() {
        return this.getServer().getPluginManager().isPluginEnabled("LiteBans");
    }

    private boolean isAdvancedBanEnabled() {
        return this.getServer().getPluginManager().isPluginEnabled("AdvancedBan");
    }

    @Override
    public void onDisable() {
        Messenger messenger = this.getServer().getMessenger();
        instance = null;
        if(getDiscordBot() != null && getDiscordBot().getJDA() != null) {
            getDiscordBot().getJDA().shutdown();
        }
        actionManager.unregisterAllActions();
        try {
            database.getConnectionSource().close();
        } catch (Exception ignored) { }

        if(messenger.isIncomingChannelRegistered(this, "BungeeCord")) {
            this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
            this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
        }
    }

    public static Database getDatabase() {
        return database;
    }

    public static BeacmcStaffWork getInstance() {
        return instance;
    }

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        mainConfig = new MainConfiguration(new File(getDataFolder(), "config.yml"));
    }

    public static AddonManager getAddonManager() {
        return addonManager;
    }

    public static StaffWorkManager getStaffWorkManager() {
        return staffWorkManager;
    }
    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public static DiscordBot getDiscordBot() {
        return discordBot;
    }

    public static MainConfiguration getMainConfig() {
        return mainConfig;
    }

    public static ActionManager getActionManager() {
        return actionManager;
    }
}
