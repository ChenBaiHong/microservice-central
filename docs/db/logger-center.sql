/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库01
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:33306
 Source Schema         : logger-center

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 26/07/2020 18:48:52
*/
CREATE DATABASE IF NOT EXISTS `logger-center` DEFAULT CHARACTER SET = utf8mb4;
Use `logger-center`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_logger
-- ----------------------------
DROP TABLE IF EXISTS `sys_logger`;
CREATE TABLE `sys_logger` (
  `id` int NOT NULL AUTO_INCREMENT,
  `application_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '应用名',
  `class_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类名',
  `method_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '方法名',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `client_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '租户id',
  `operation` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作信息',
  `timestamp` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_logger
-- ----------------------------
BEGIN;
INSERT INTO `sys_logger` VALUES (1, 'microservice-platform-started', 'com.baimicro.central.platform.system.controller.PlatfUserController', 'edit', 1, 'admin', 'micro-platform-pc', '更新用户:{\"birthday\":\"\",\"sex\":\"男\",\"updateTime\":\"2020-07-26 12:30:15\",\"delFlag\":0,\"enabled\":1,\"realname\":\"吾乃二战神\",\"createBy\":\"admin\",\"password\":\"$2a$10$5SIcPeBmw9XOc1BVin3Y3uH5/.XjFLPNGqnPv1S8wil5vD0A/CA4G\",\"createTime\":\"2020-05-04 19:35:18\",\"phone\":\"13550468890\",\"updateBy\":\"admin\",\"tenantId\":\"micro-platform-pc\",\"selectedroles\":\"1\",\"id\":1,\"email\":\"chen.baigle@gmail.com\",\"username\":\"admin\"}', '2020-07-26 12:50:24.32');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
