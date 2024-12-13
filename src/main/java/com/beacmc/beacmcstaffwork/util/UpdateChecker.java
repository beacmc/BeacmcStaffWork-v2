package com.beacmc.beacmcstaffwork.util;


import com.beacmc.beacmcstaffwork.BeacmcStaffWork;
import com.beacmc.beacmcstaffwork.config.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class UpdateChecker {

    public static void startCheck() {
        if (!Config.getBoolean("settings.update-check"))
            return;

        String version = UpdateChecker.getActualVersion();
        if (!version.equals(BeacmcStaffWork.getInstance().getDescription().getVersion())) {
            ArrayList<String> list = new ArrayList<>(Config.getStringList("settings.messages.update-check-console"));
            list.forEach(execute ->
                    System.out.println(Color.compile(execute).replace("{current_version}", BeacmcStaffWork.getInstance().getDescription().getVersion()).replace("{latest_version}", version)));
        }
    }

    public static String getActualVersion() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=112394").openConnection();

            int timeout = 1750;
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            String version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
            return version;
        } catch (Exception exception) {
            return "";
        }
    }
}
