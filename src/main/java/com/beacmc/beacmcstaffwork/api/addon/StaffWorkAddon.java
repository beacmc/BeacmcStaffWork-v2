package com.beacmc.beacmcstaffwork.api.addon;

import com.beacmc.beacmcstaffwork.BeacmcStaffWork;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;

public class StaffWorkAddon {

    private String name;
    private String version;
    private File file;
    private ClassLoader classLoader;
    private URLClassLoader urlClassLoader;
    private BeacmcStaffWork beacmcStaffWork;
    private boolean enabled;

    public StaffWorkAddon() {
    }

    public final void initialize(String name, String version, File file, URLClassLoader urlClassLoader) {
        this.name = name;
        this.version = version;
        this.file = file;
        this.enabled = false;
        this.beacmcStaffWork = BeacmcStaffWork.getInstance();
        this.urlClassLoader = urlClassLoader;
        classLoader = getClass().getClassLoader();
        if (!(classLoader instanceof AddonClassLoader))  {
            throw new IllegalArgumentException("StaffWorkAddon requires " + AddonClassLoader.class.getName());
        }
    }

    public void onEnable() {
    }


    public void onDisable() {
    }

    public URL getResource(String file) {
        return urlClassLoader.findResource(file);
    }

    public void saveResource(String resourcePath) {
        if (resourcePath.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = null;
        try {
            in = getResource(resourcePath).openStream();
        } catch (IOException ignored) { }
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + file);
        }

        File outFile = new File(getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        try {
            if (!outFile.exists()) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        } catch (IOException ex) {
            System.out.println("Could not save " + outFile.getName() + " to " + outFile);
        }
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public URLClassLoader getUrlClassLoader() {
        return urlClassLoader;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public File getDataFolder() {
        File dir = new File(beacmcStaffWork.getDataFolder(), "addons" + File.separator + name);
        if (!dir.exists())
            dir.mkdirs();

        return dir;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;

            if (this.enabled) onEnable();

            else onDisable();
        }
    }

    public BeacmcStaffWork getBeacmcStaffWork() {
        return beacmcStaffWork;
    }
}
