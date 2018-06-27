package com.solarchargerdatamonitor.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Zeeshan on 16/10/2017.
 */
public class DeviceDao {

    final static Logger logger = Logger.getLogger(DeviceDao.class);

    //*******************************
    //SELECT Devices
    //*******************************
    public static ObservableList<Device> selectDevices () throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM devices";

        ResultSet rsDevices = null;

        //Execute SELECT statement
        try {

            //Get ResultSet from dbExecuteQuery method
            rsDevices = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getDevicesList method and get Device object
            ObservableList<Device> deviceList = getDevicesList(rsDevices);
            logger.info("Result set size: " + deviceList.size());

            //Return device object
            return deviceList;
        } catch (SQLException e) {
            logger.error("SQL select operation has been failed: ", e);
            //Return exception
            throw e;
        } finally {
            if(rsDevices != null) {
                rsDevices.close();
            }
        }
    }

    //*******************************
    //SELECT Devices
    //*******************************
    public static ObservableList<Device> selectDevicesForComboBox () throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM devices";

        ResultSet rsDevices = null;

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            rsDevices = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getDevicesList method and get Device object
            ObservableList<Device> deviceList = getDevicesList(rsDevices);

            Device allDeviceOption = new Device();
            allDeviceOption.setId(0);
            allDeviceOption.setName("All");
            deviceList.add(0, allDeviceOption);

            logger.info("Result set size: " + deviceList.size());

            //Return device object
            return deviceList;
        } catch (SQLException e) {
            logger.error("SQL select operation has been failed: ", e);
            //Return exception
            throw e;
        } finally {
            if(rsDevices != null) {
                rsDevices.close();
            }
        }
    }

    //Select * from devices operation
    private static ObservableList<Device> getDevicesList(ResultSet rs) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //Declare a observable List which comprises of Devices objects
        ObservableList<Device> deviceList = FXCollections.observableArrayList();

        while (rs.next()) {
            Device device = new Device();
            device.setId(rs.getInt("Id"));
            device.setName(rs.getString("name"));
            device.setIP(rs.getString("IP"));
            device.setPort(rs.getInt("port"));
            device.setPowerControl(rs.getString("powerControl"));
            device.setConnectionStatus(rs.getInt("connectionStatus"));
            device.setDeviceStatus(rs.getInt("deviceStatus"));

            //Add device to the ObservableList
            deviceList.add(device);
        }
        //return deviceList (ObservableList of Devices)
        return deviceList;
    }

    //*******************************
    //SELECT Latest Log
    //*******************************
    public static ResultSet getLatestLog () throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM `devices` AS d \n" +
                 " LEFT JOIN ( SELECT `registers_log`.`deviceId` as deviceId, `batteryVoltage_4115`, `inputVoltage_4116`, \n" +
                 " `batteryCurrent_4117`, `powerToBattery_4119`, `comboChargeStage_4120`, `pvInputCurrent_4121`, \n" +
                 " `FET_Temperature_4133`, `PCB_Temperature_4134` FROM `registers_log` \n" +
                 " INNER JOIN ( SELECT gl.deviceId, MAX(gl.dateLogged) AS latest, MAX(gl.registersLogId) AS maxId \n" +
                 " FROM `registers_log` AS gl GROUP BY gl.deviceId ) AS rssec \n" +
                 " ON `registers_log`.registersLogId = rssec.maxId )\n" +
                 " AS registeresLog ON d.Id = registeresLog.deviceId";

        return DBUtil.dbExecuteQuery(selectStmt);
    }

    //*******************************
    //SELECT Filtered Log
    //*******************************
    public static ResultSet getFilteredLog (int deviceId, String fromDate, String toDate) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM registers_log AS rl\n" +
                " LEFT OUTER JOIN devices AS d ON d.Id = rl.deviceId\n" +
                " WHERE rl.dateLogged BETWEEN '" + fromDate + " 00:00:00' AND '" + toDate + " 23:59:59'\n";

        if(deviceId != 0){
            selectStmt += " AND rl.deviceId = " + deviceId;
        }

        return DBUtil.dbExecuteQuery(selectStmt);
    }

}
