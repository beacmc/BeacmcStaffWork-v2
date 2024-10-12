package com.beacmc.beacmcstaffwork;

import com.beacmc.beacmcstaffwork.util.action.*;
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
import com.beacmc.beacmcstaffwork.work.StaffWorkManager;
import com.beacmc.beacmcstaffwork.player.StaffPlayer;
import com.beacmc.beacmcstaffwork.util.Color;
import com.beacmc.beacmcstaffwork.hook.litebans.LiteBansHandler;
import com.beacmc.beacmcstaffwork.util.UpdateChecker;
import com.beacmc.beacmcstaffwork.config.Config;
import com.beacmc.beacmcstaffwork.util.messaging.MessagingListener;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.util.*;

public final class BeacmcStaffWork extends JavaPlugin {

    private static LuckPerms luckPerms;
    private static DiscordBot discordBot;
    private static BeacmcStaffWork instance;
    private static Database database;
    private static ActionManager actionManager;
    private static StaffWorkManager staffWorkManager;
    private static HashSet<StaffPlayer> users;


    @Override
    public void onEnable() {
        if(!this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.getLogger().severe("Установите плагин PlaceholderAPI для работы плагина");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        if(!this.getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            this.getLogger().severe("Установите плагин LuckPerms для работы плагина");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        saveDefaultConfig();
        users = new HashSet<>();
        Messenger messenger = this.getServer().getMessenger();

        instance = this;
        database = new Database();
        database.connect();
        actionManager = new ActionManager();

        actionManager.registerActions(new ConsoleAction(), new PlayerAction(), new SoundAction(), new MessageToModeratosAction(), new MessageAction(), new BroadcastAction(), new ActionbarAction());
        staffWorkManager = new StaffWorkManager();
        update();

        new PlaceholderHook().load();
        luckPerms = this.getServer().getServicesManager().load(LuckPerms.class);

        new StaffCommand();
        new StaffAdminCommand();
        new StaffChatCommand();
        if (Config.getBoolean("settings.discord.enable")) {
            new LibraryManager(this);
            discordBot = new DiscordBot();
            discordBot.connect();
        }

        this.getCommand("staffwork").setTabCompleter(new StaffCompleter());
        this.getCommand("staffworkadmin").setTabCompleter(new StaffAdminCompleter());

        if (Config.getBoolean("settings.proxy")) {
            messenger.registerIncomingPluginChannel(this, "BungeeCord", new MessagingListener());
            messenger.registerOutgoingPluginChannel(this, "BungeeCord");
        }

        if(isLiteBansEnabled() && !isAdvancedBanEnabled()) {
            new LiteBansHandler().register();
        }

        if(isAdvancedBanEnabled() && !isLiteBansEnabled()) {
            this.getServer().getPluginManager().registerEvents(new BanListener(), this);
        }

        this.getServer().getPluginManager().registerEvents(new MainListener(), this);
        this.getServer().getPluginManager().registerEvents(new PluginListener(), this);
        this.getServer().getPluginManager().registerEvents(new CommandListener(), this);
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


    private void update() {
        if (!this.getConfig().getBoolean("settings.update-check"))
            return;

        String latest = UpdateChecker.start();
        if (!latest.equals(this.getDescription().getVersion())) {
            ArrayList<String> list = new ArrayList<>(Config.getStringList("settings.messages.update-check-console"));
            list.forEach(execute ->
                System.out.println(Color.compile(execute).replace("{current_version}", this.getDescription().getVersion()).replace("{latest_version}", latest)));
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
        super.reloadConfig();
        Config.reload();
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

    public static HashSet<StaffPlayer> getUsers() {
        return users;
    }

    public static ActionManager getActionManager() {
        return actionManager;
    }
}
