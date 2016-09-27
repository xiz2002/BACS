CREATE DATABASE  IF NOT EXISTS `acs` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `acs`;
-- MySQL dump 10.13  Distrib 5.7.9, for osx10.9 (x86_64)
--
-- Host: 192.168.0.8    Database: acs
-- ------------------------------------------------------
-- Server version	5.5.44-MariaDB-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary view structure for view `ACS_PROFESSORS_COURSE_VW`
--

DROP TABLE IF EXISTS `ACS_PROFESSORS_COURSE_VW`;
/*!50001 DROP VIEW IF EXISTS `ACS_PROFESSORS_COURSE_VW`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `ACS_PROFESSORS_COURSE_VW` AS SELECT 
 1 AS `PROF_ID`,
 1 AS `COU_CODE`,
 1 AS `COU_NAME`,
 1 AS `DEP_NAME`,
 1 AS `LOC_CODE`,
 1 AS `COU_DAY`,
 1 AS `COU_ST`,
 1 AS `COU_ED`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `ACS_COURSES_VW`
--

DROP TABLE IF EXISTS `ACS_COURSES_VW`;
/*!50001 DROP VIEW IF EXISTS `ACS_COURSES_VW`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `ACS_COURSES_VW` AS SELECT 
 1 AS `COU_CODE`,
 1 AS `COU_NAME`,
 1 AS `DEP_NAME`,
 1 AS `LOC_NAME`,
 1 AS `COU_DAY`,
 1 AS `COU_ST`,
 1 AS `COU_EN`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `ACS_USERS_COURSE_VW`
--

DROP TABLE IF EXISTS `ACS_USERS_COURSE_VW`;
/*!50001 DROP VIEW IF EXISTS `ACS_USERS_COURSE_VW`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `ACS_USERS_COURSE_VW` AS SELECT 
 1 AS `USER_ID`,
 1 AS `COU_CODE`,
 1 AS `COU_NAME`,
 1 AS `DEP_NAME`,
 1 AS `LOC_CODE`,
 1 AS `COU_DAY`,
 1 AS `COU_ST`,
 1 AS `COU_ED`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `ACS_LOGS_VW`
--

DROP TABLE IF EXISTS `ACS_LOGS_VW`;
/*!50001 DROP VIEW IF EXISTS `ACS_LOGS_VW`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `ACS_LOGS_VW` AS SELECT 
 1 AS `USERID`,
 1 AS `USERNAME`,
 1 AS `LOCATIONNAME`,
 1 AS `DATE`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `ACS_PROFESSORS_COURSE_VW`
--

/*!50001 DROP VIEW IF EXISTS `ACS_PROFESSORS_COURSE_VW`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `ACS_PROFESSORS_COURSE_VW` AS select `a`.`PROF_ID` AS `PROF_ID`,`a`.`COURSE_ID` AS `COU_CODE`,`b`.`NAME` AS `COU_NAME`,`c`.`NAME` AS `DEP_NAME`,`b`.`LOC_CODE` AS `LOC_CODE`,`b`.`DAY` AS `COU_DAY`,`b`.`START` AS `COU_ST`,`b`.`END` AS `COU_ED` from ((`ACS_PROFESSORS_SCHEDULE_TB` `a` join `ACS_COURSES_TB` `b`) join `ACS_DEPARTMENTS_TB` `c`) where ((`a`.`COURSE_ID` = `b`.`CODE`) and (`b`.`DEP_ID` = `c`.`CODE`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `ACS_COURSES_VW`
--

/*!50001 DROP VIEW IF EXISTS `ACS_COURSES_VW`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `ACS_COURSES_VW` AS select `a`.`CODE` AS `COU_CODE`,`a`.`NAME` AS `COU_NAME`,`b`.`NAME` AS `DEP_NAME`,`c`.`NAME` AS `LOC_NAME`,`a`.`DAY` AS `COU_DAY`,`a`.`START` AS `COU_ST`,`a`.`END` AS `COU_EN` from ((`ACS_COURSES_TB` `a` join `ACS_DEPARTMENTS_TB` `b`) join `ACS_LOCATIONS_TB` `c`) where ((`a`.`DEP_ID` = `b`.`CODE`) and (`a`.`LOC_CODE` = `c`.`CODE`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `ACS_USERS_COURSE_VW`
--

/*!50001 DROP VIEW IF EXISTS `ACS_USERS_COURSE_VW`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `ACS_USERS_COURSE_VW` AS select `a`.`USER_ID` AS `USER_ID`,`a`.`COURSE_ID` AS `COU_CODE`,`b`.`NAME` AS `COU_NAME`,`c`.`NAME` AS `DEP_NAME`,`b`.`LOC_CODE` AS `LOC_CODE`,`b`.`DAY` AS `COU_DAY`,`b`.`START` AS `COU_ST`,`b`.`END` AS `COU_ED` from ((`ACS_USERS_SCHEDULE_TB` `a` join `ACS_COURSES_TB` `b`) join `ACS_DEPARTMENTS_TB` `c`) where ((`a`.`COURSE_ID` = `b`.`CODE`) and (`b`.`DEP_ID` = `c`.`CODE`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `ACS_LOGS_VW`
--

/*!50001 DROP VIEW IF EXISTS `ACS_LOGS_VW`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `ACS_LOGS_VW` AS select `a`.`USERID` AS `USERID`,`b`.`NAME` AS `USERNAME`,`c`.`NAME` AS `LOCATIONNAME`,`a`.`TIME` AS `DATE` from ((`ACS_ACCESS_LOGS_TB` `a` join `ACS_USERS_TB` `b`) join `ACS_LOCATIONS_TB` `c`) where ((`a`.`USERID` = `b`.`ID`) and (`a`.`LOC_CODE` = `c`.`CODE`)) order by `a`.`TIME` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Dumping events for database 'acs'
--

--
-- Dumping routines for database 'acs'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-19  1:36:34
