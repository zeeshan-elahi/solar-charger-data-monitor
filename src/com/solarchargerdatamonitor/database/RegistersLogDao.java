package com.solarchargerdatamonitor.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Zeeshan on 16/10/2017.
 */
public class RegistersLogDao {

    final static Logger logger = Logger.getLogger(RegistersLogDao.class);

    //*******************************
    //SELECT RegisterLog
    //*******************************
    public static RegistersLog selectRegistersLogForDevice (int deviceId) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM registers_Log where deviceId = " + deviceId + " Order by dateLogged desc FETCH FIRST ROW ONLY";

        ResultSet rsRegistersLog = null;

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            rsRegistersLog = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getRegistersLog method and get RegistersLog object
            RegistersLog registersLog = getRegistersLog(rsRegistersLog);

            //Return device object
            return registersLog;
        } catch (SQLException e) {
            logger.error("SQL select operation has been failed: ", e);
            //Return exception
            throw e;
        } finally {
            rsRegistersLog.close();
        }
    }

    //Select * from devices operation
    private static RegistersLog getRegistersLog(ResultSet rs) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        RegistersLog registersLog = null;

        while (rs.next()) {
            registersLog = new RegistersLog();
            registersLog.setRegistersLogId(rs.getInt("registersLogId"));
            registersLog.setDeviceId(rs.getInt("deviceId"));
            registersLog.setRegister4115(rs.getDouble("4115"));
            registersLog.setRegister4116(rs.getDouble("4116"));
            registersLog.setRegister4117(rs.getDouble("4117"));
            registersLog.setRegister4119(rs.getDouble("4119"));
            registersLog.setRegister4120(rs.getString("4120"));
            registersLog.setRegister4121(rs.getDouble("4121"));
            registersLog.setRegister4133(rs.getDouble("4133"));
            registersLog.setRegister4134(rs.getDouble("4134"));
        }

        return registersLog;
    }

}
