package com.beacmc.beacmcstaffwork.lib;

import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.Library;
import org.bukkit.plugin.Plugin;


public class LibraryManager {

    private final Library JDA = Library.builder()
            .groupId("net{}dv8tion")
            .artifactId("JDA")
            .version("5.0.0-beta.20")
            .resolveTransitiveDependencies(true)
            .excludeTransitiveDependency("club{}minnced", "opus-java")
            .build();


    public LibraryManager(Plugin plugin) {
        BukkitLibraryManager manager = new BukkitLibraryManager(plugin);
        manager.addMavenCentral();
        manager.loadLibrary(JDA);
    }
}
