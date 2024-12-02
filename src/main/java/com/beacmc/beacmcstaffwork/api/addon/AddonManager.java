package com.beacmc.beacmcstaffwork.api.addon;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class AddonManager {

    private final Map<String, StaffWorkAddon> addons;
    private final Logger logger;

    public AddonManager() {
        addons = new HashMap<>();
        logger = BeacmcStaffWork.getInstance().getLogger();
    }

    public void loadAddons() {
        File addonsDir = new File(BeacmcStaffWork.getInstance().getDataFolder(), "addons");
        File[] files = addonsDir.listFiles();

        if(!addonsDir.exists()) {
            addonsDir.mkdirs();
        }

        if(files == null) return;

        for (File file : files) {
            loadAddon(file);
        }
    }

    public void loadAddon(File file) {
        if (!file.isFile() || !file.getName().endsWith(".jar")) return;

        try (JarFile jar = new JarFile(file)) {
            JarEntry entry = jar.getJarEntry("addon.yml");
            Yaml yaml = new Yaml();
            Map<String, Object> map = yaml.load(jar.getInputStream(entry));

            String name = (String) map.get("name");
            String version = (String) map.get("version");
            String main = (String) map.get("main");

            if (isLoad(name))
                return;

            AddonClassLoader loader = new AddonClassLoader(file, BeacmcStaffWork.getInstance().getClass().getClassLoader());

            try {
                Class<?> clazz = Class.forName(main, true, loader);
                StaffWorkAddon addon = (StaffWorkAddon) clazz.getDeclaredConstructor().newInstance();
                addon.initialize(name, version, file, loader);
                addons.put(name, addon);
                enableAddon(addon);
            } catch (ClassNotFoundException e) {
                logger.severe("Main class not found!");
            } catch (ClassCastException e) {
                logger.severe("Inheritance of the StaffWorkAddon class is required for the addon to work");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableAddon(StaffWorkAddon addon) {
        addon.setEnabled(true);
    }

    public void disableAddon(StaffWorkAddon addon) {
        addon.setEnabled(false);
    }

    public void unloadAddons() {
        this.getAddons().forEach(this::unloadAddon);
    }

    public void unloadAddon(StaffWorkAddon addon) {
        try {
            disableAddon(addon);
            addons.remove(addon.getName());
            closeURLClassLoader(addon.getUrlClassLoader());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public boolean isLoad(StaffWorkAddon addon) {
        return addons.containsValue(addon);
    }

    public boolean isLoad(String addon) {
        return addons.containsKey(addon);
    }

    private void closeURLClassLoader(URLClassLoader loader) {
        try {
            loader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<StaffWorkAddon> getAddons() {
        return addons.values();
    }
}
