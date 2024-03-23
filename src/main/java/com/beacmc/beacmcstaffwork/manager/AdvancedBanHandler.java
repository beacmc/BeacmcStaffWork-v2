package com.beacmc.beacmcstaffwork.manager;

import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;

public class AdvancedBanHandler {

    private Punishment punishment;

    public AdvancedBanHandler(Punishment punishment) {
        this.punishment = punishment;
    }

    private static String type;
    private static String executor;

    public void start() {

        executor = punishment.getOperator();
        PunishmentType punishmentType = punishment.getType();


        switch (punishmentType) {
            case BAN: {
                type = "ban";
                break;
            }
            case TEMP_BAN: {
                type = "ban";
                break;
            }
            case IP_BAN: {
                type = "ban";
                break;
            }
            case TEMP_IP_BAN: {
                type = "ban";
                break;
            }
            case TEMP_MUTE: {
                type = "mute";
                break;
            }
            case MUTE: {
                type = "mute";
                break;
            }
            case KICK: {
                type = "kick";
                break;
            }
            default: {
                type = null;
                break;
            }
        }
    }

    public String getType() {
        return type;
    }

    public String getExecutor() {
        return executor;
    }
}