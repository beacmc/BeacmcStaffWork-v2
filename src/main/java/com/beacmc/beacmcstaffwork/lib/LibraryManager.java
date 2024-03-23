package com.beacmc.beacmcstaffwork.lib;

import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.Library;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class LibraryManager {

    private final Library JDA = Library.builder()
            .groupId("net{}dv8tion")
            .artifactId("JDA")
            .version("5.0.0-beta.20")
            .resolveTransitiveDependencies(true)
            .excludeTransitiveDependency("club{}minnced", "opus-java")
            .build();

    private final Library ORMLITE = Library.builder()
            .groupId("com{}j256{}ormlite")
            .artifactId("ormlite-jdbc")
            .version("6.1")
            .relocate("com{}j256{}ormlite", "com{}beacmc{}beacmcstaffwork{}lib{}com{}j256{}ormlite")
            .build();

    public LibraryManager(Plugin plugin) {
        List<Library> libraries = List.of(JDA, ORMLITE);
        BukkitLibraryManager manager = new BukkitLibraryManager(plugin);
        manager.addMavenCentral();
        manager.addJitPack();
        for (Library lib : libraries) {
            manager.loadLibrary(lib);
        }
    }
}
