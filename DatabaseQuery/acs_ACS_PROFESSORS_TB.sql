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
-- Table structure for table `ACS_PROFESSORS_TB`
--

DROP TABLE IF EXISTS `ACS_PROFESSORS_TB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ACS_PROFESSORS_TB` (
  `CODE` varchar(9) NOT NULL COMMENT '고유코드',
  `ID` varchar(9) NOT NULL DEFAULT '' COMMENT '사용자아이디',
  `NAME` varchar(12) NOT NULL DEFAULT '' COMMENT '이름',
  `PASSWD` varchar(255) NOT NULL DEFAULT '' COMMENT '비밀번호',
  `DEP_CODE` varchar(3) NOT NULL DEFAULT '' COMMENT '학과코드',
  `STATUS` int(1) NOT NULL DEFAULT '0',
  `VERSIOND` int(11) NOT NULL,
  PRIMARY KEY (`CODE`),
  UNIQUE KEY `ID_FK` (`ID`),
  KEY `FK_PROFESOR_DEP` (`DEP_CODE`),
  CONSTRAINT `FK_PROFESOR_DEP` FOREIGN KEY (`DEP_CODE`) REFERENCES `ACS_DEPARTMENTS_TB` (`CODE`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ACS_PROFESSORS_TB`
--

LOCK TABLES `ACS_PROFESSORS_TB` WRITE;
/*!40000 ALTER TABLE `ACS_PROFESSORS_TB` DISABLE KEYS */;
INSERT INTO `ACS_PROFESSORS_TB` VALUES ('000000000','TEST','TEST','qUqP5cyxm6YcTAhz05Hph5gvu9M=','350',0,1);
/*!40000 ALTER TABLE `ACS_PROFESSORS_TB` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-19  1:36:34
