-- create database
-- CREATE DATABASE spring_study_db DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_unicode_ci;

-- create table
CREATE TABLE `Account` (
  `accountNo` varchar(20) NOT NULL,
  `balance` bigint(20) NOT NULL,
  `lastPaidOn` datetime NOT NULL,
  PRIMARY KEY (`accountNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `Account` VALUES ('100', '0', '2008-09-01 00:00:00'), ('200', '100', '2008-08-01 00:00:00'), ('300', '-100', '2008-09-01 00:00:00');
COMMIT;