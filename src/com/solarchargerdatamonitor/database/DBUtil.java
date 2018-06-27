package com.solarchargerdatamonitor.database;

import com.sun.rowset.CachedRowSetImpl;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


/**
 * Created by Zeeshan on 16/10/2017.
 */
public class DBUtil {

    //Declare JDBC Driver
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    //Connection
    //private static Connection conn = null;

    //Connection String
    private static final String connStr;

    final static Logger logger = Logger.getLogger(DBUtil.class);

    static{

        String connectionString = "";

        try {

            InputStream jdbcPropFileInput = new FileInputStream("./jdbc.properties");
            Properties jdbcProperties = new Properties();
            jdbcProperties.load(jdbcPropFileInput);
            jdbcPropFileInput.close();

            connectionString = jdbcProperties.getProperty("jdbc.url").toString() +
                    "&useSSL=" + jdbcProperties.getProperty("jdbc.useSSL") +
                    "&user=" + jdbcProperties.getProperty("jdbc.username") +
                    "&password=" + jdbcProperties.getProperty("jdbc.password");

        } catch (Exception ex){
            logger.error("Error reading jdbc.properties file: ", ex);
        } finally {
            connStr = connectionString;
        }
    }

    //Connect to DB
    public static Connection dbConnect() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        Connection connection;

        //Setting Oracle JDBC Driver
        try {
            Class.forName(JDBC_DRIVER).newInstance();
        } catch (InstantiationException e) {
            logger.error("Unable to create new instance of JDBC Driver.", e);
            throw e;
        } catch (IllegalAccessException e) {
            logger.error("Unable to access JDBC Driver.", e);
            throw e;
        } catch (ClassNotFoundException e) {
            logger.error("Unable to find required JDBC Driver?", e);
            throw e;
        }

        logger.info("JDBC Driver Registered!");

        //Establish the Oracle Connection using Connection String
        try {
            logger.info("Getting connection from database");
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setURL(connStr);
            //connection = DriverManager.getConnection(connStr);
            connection = mysqlDataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Connection Failed! Check output console", e);
            throw e;
        }

        return connection;
    }

    //Close Connection
    public static void dbDisconnect(Connection connection) throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                logger.info("Closing connection with database");
                connection.close();
            }
        } catch (Exception e){
            logger.error("", e);
            throw e;
        }
    }

    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        Connection connection = null;

        try {
            //Connect to DB
            connection = dbConnect();
            logger.info("Select statement: " + queryStmt + "\n");

            //Create statement
            stmt = connection.createStatement();

            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);

            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            logger.error("Problem occurred at executeQuery operation : ", e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect(connection);
        }
        //Return CachedRowSet
        return crs;
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //Declare statement as null
        Statement stmt = null;
        Connection connection = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            connection = dbConnect();
            //Create Statement
            stmt = connection.createStatement();
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            logger.error("Problem occurred at executeUpdate operation : ", e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect(connection);
        }
    }
}
