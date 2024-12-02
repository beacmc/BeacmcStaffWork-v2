package com.beacmc.beacmcstaffwork.api.addon;

import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class AddonClassLoader extends URLClassLoader {
    public AddonClassLoader(File file, ClassLoader parent) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, parent);
    }
}
