-- create database
-- CREATE DATABASE spring_study_db DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_unicode_ci;

-- create table
CREATE TABLE `Account` (
  `accountNo` varchar(20) NOT NULL,
  `balance` bigint(20) NOT NULL,
  `lastPaidOn` datetime NOT NULL,
  PRIMARY KEY (`accountNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;