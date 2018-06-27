-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.18-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for solarchargerregisterslog
DROP DATABASE IF EXISTS `solarchargerregisterslog`;
CREATE DATABASE IF NOT EXISTS `solarchargerregisterslog` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `solarchargerregisterslog`;

-- Dumping structure for table solarchargerregisterslog.devices
DROP TABLE IF EXISTS `devices`;
CREATE TABLE IF NOT EXISTS `devices` (
  `Id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `IP` varchar(16) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `powerControl` varchar(16) DEFAULT NULL,
  `connectionStatus` int(1) DEFAULT NULL,
  `dateLastUpdated` datetime DEFAULT NULL,
  `deviceStatus` int(1) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table solarchargerregisterslog.devices: ~9 rows (approximately)
DELETE FROM `devices`;
/*!40000 ALTER TABLE `devices` DISABLE KEYS */;
INSERT INTO `devices` (`Id`, `name`, `IP`, `port`, `powerControl`, `connectionStatus`, `dateLastUpdated`, `deviceStatus`) VALUES
	(1, 'Device 1', '192.168.0.2', 502, '192.168.0.2', 0, '2017-12-21 01:57:57', 1),
	(2, 'Device 2', '192.168.0.3', 502, '192.168.0.3', 1, '2017-12-21 01:57:57', 1),
	(3, 'Device 3', '192.168.0.4', 502, '192.168.0.4', 0, '2017-12-21 01:57:57', 2);
/*!40000 ALTER TABLE `devices` ENABLE KEYS */;

-- Dumping structure for table solarchargerregisterslog.registers_log
DROP TABLE IF EXISTS `registers_log`;
CREATE TABLE IF NOT EXISTS `registers_log` (
  `registersLogId` int(11) NOT NULL AUTO_INCREMENT,
  `deviceId` int(11) DEFAULT NULL,
  `dateLogged` datetime DEFAULT NULL,
  `infoFlags_4104` double DEFAULT NULL,
  `batteryVoltage_4115` double DEFAULT NULL,
  `inputVoltage_4116` double DEFAULT NULL,
  `batteryCurrent_4117` double DEFAULT NULL,
  `energyToBattery_4118` double DEFAULT NULL,
  `powerToBattery_4119` double DEFAULT NULL,
  `comboChargeStage_4120` varchar(100) DEFAULT NULL,
  `pvInputCurrent_4121` double DEFAULT NULL,
  `ampHours_4125` double DEFAULT NULL,
  `infoFlagsLSB_4130` double DEFAULT NULL,
  `infoFlagsMSB_4131` double DEFAULT NULL,
  `FET_Temperature_4133` double DEFAULT NULL,
  `PCB_Temperature_4134` double DEFAULT NULL,
  `niteMinutesNoPower_4135` double DEFAULT NULL,
  `inputCurrent_4272` double DEFAULT NULL,
  `batteryVoltage_4276` double DEFAULT NULL,
  `pvInputVoltage_4277` double DEFAULT NULL,
  `wbJrAmpHourNetLSB_4369` double DEFAULT NULL,
  `wbJrAmpHourNetMSB_4370` double DEFAULT NULL,
  `wbJrCurrent_4371` double DEFAULT NULL,
  PRIMARY KEY (`registersLogId`)
) ENGINE=InnoDB AUTO_INCREMENT=9383 DEFAULT CHARSET=utf8;

-- Dumping data for table solarchargerregisterslog.registers_log: ~9,269 rows (approximately)
DELETE FROM `registers_log`;
/*!40000 ALTER TABLE `registers_log` DISABLE KEYS */;
INSERT INTO `registers_log` (`registersLogId`, `deviceId`, `dateLogged`, `infoFlags_4104`, `batteryVoltage_4115`, `inputVoltage_4116`, `batteryCurrent_4117`, `energyToBattery_4118`, `powerToBattery_4119`, `comboChargeStage_4120`, `pvInputCurrent_4121`, `ampHours_4125`, `infoFlagsLSB_4130`, `infoFlagsMSB_4131`, `FET_Temperature_4133`, `PCB_Temperature_4134`, `niteMinutesNoPower_4135`, `inputCurrent_4272`, `batteryVoltage_4276`, `pvInputVoltage_4277`, `wbJrAmpHourNetLSB_4369`, `wbJrAmpHourNetMSB_4370`, `wbJrCurrent_4371`) VALUES
	(1, 1, '2017-12-20 15:23:42', 15, 53.5, 109.4, 0.3, 0.9, 16, 'Max Power Point Tracking. Seeking Float set point Voltage - MPPT or Regulating Voltage', 0.3, 17, 4100, -19968, 43.9, 44, 0, 4.8, 53.8, 109.4, 64, 0, -5),
	(2, 2, '2017-12-20 15:23:42', 15, 54.6, 122, 1, 1.1, 54, 'Battery is FULL and regulating battery voltage at Float Set point - MPPT or Regulating Voltage', 0.6, 20, 4100, -19968, 40.1, 42.2, 0, 5.5, 54.9, 122.2, 69, 0, 0),
	(3, 3, '2017-12-20 15:23:46', 15, 54.6, 122, 1.1, 1.1, 60, 'Battery is FULL and regulating battery voltage at Float Set point - MPPT or Regulating Voltage', 0.6, 20, 4100, -19968, 40.1, 42.2, 0, 5.5, 54.9, 121.6, 69, 0, 0);
/*!40000 ALTER TABLE `registers_log` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
