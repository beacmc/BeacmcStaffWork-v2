package com.beacmc.beacmcstaffwork.lib;

import com.alessiodp.libby.Library;
import org.bukkit.entity.LightningStrike;

import java.util.List;

public class Libraries {

    public static final Library JDA = Library.builder()
            .groupId("net{}dv8tion")
            .artifactId("JDA")
            .version("5.0.0-beta.20")
            .resolveTransitiveDependencies(true)
            .excludeTransitiveDependency("club{}minnced", "opus-java")
            .build();

    public static final Library ORMLITE = Library.builder()
            .groupId("com.j256.ormlite")
            .artifactId("ormlite-jdbc")
            .version("6.1")
            .build();

    public static List<Library> getAsList() {
        return List.of(ORMLITE, JDA);
    }
}
