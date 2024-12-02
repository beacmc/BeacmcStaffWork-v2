package com.beacmc.beacmcstaffwork.lib;

import com.alessiodp.libby.BukkitLibraryManager;
import org.bukkit.plugin.Plugin;


public class LibraryManager {

    public LibraryManager(Plugin plugin) {
        BukkitLibraryManager manager = new BukkitLibraryManager(plugin);
        manager.addMavenCentral();
        Libraries.getAsList().forEach(manager::loadLibrary);
    }
}
