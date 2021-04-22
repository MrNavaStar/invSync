package com.mrnavastar.invsync.setup;

import com.mrnavastar.invsync.api.event.PlayerJoinCallback;
import com.mrnavastar.invsync.api.event.PlayerLeaveCallback;
import com.mrnavastar.invsync.conversion.PlayerDataConversion;
import com.mrnavastar.invsync.sql.Table;
import com.mrnavastar.invsync.sql.column.PlayerDataColumns;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.Level;

import static com.mrnavastar.invsync.Invsync.log;

public class PlayerDataSetup {

    public static Table playerDataTable;
    public static String tableName;

    public static void getConfigData() {
        tableName = ConfigManager.Player_Data_Table_Name;
    }

    public static void subToEvents() {
        PlayerJoinCallback.EVENT.register((player, server) -> {
            PlayerDataConversion.sqlToNbt(player);
            log(Level.INFO, "Getting Player Data From database");
            return ActionResult.PASS;
        });

        PlayerLeaveCallback.EVENT.register((player, server) -> {
            PlayerDataConversion.nbtToSql(player);
            log(Level.INFO, "Saving Player Data to database");
            return ActionResult.PASS;
        });
    }

    public static void start() {
        getConfigData();
        playerDataTable = new Table(tableName, PlayerDataColumns.getColumns());
        subToEvents();
    }
}