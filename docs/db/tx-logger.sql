/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库01
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:33306
 Source Schema         : tx-logger

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 10/07/2020 08:45:50
*/

CREATE DATABASE IF NOT EXISTS `tx-logger` DEFAULT CHARACTER SET = utf8mb4;
Use `tx-logger`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_logger
-- ----------------------------
DROP TABLE IF EXISTS `t_logger`;
CREATE TABLE `t_logger` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `tag` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
