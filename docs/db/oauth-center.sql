/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库01
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:33306
 Source Schema         : oauth-center

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 26/07/2020 18:50:24
*/
CREATE DATABASE IF NOT EXISTS `oauth-center` DEFAULT CHARACTER SET = utf8mb4;
Use `oauth-center`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `client_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用标识',
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资源限定串(逗号分割)',
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '应用密钥(bcyt) 加密',
  `client_secret_str` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '应用密钥(明文)',
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '范围',
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '5种oauth授权方式(authorization_code,password,refresh_token,client_credentials)',
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '回调地址 ',
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限',
  `access_token_validity` int DEFAULT NULL COMMENT 'access_token有效期',
  `refresh_token_validity` int DEFAULT NULL COMMENT 'refresh_token有效期',
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '{}' COMMENT '{}',
  `autoapprove` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'true' COMMENT '是否自动授权 是-true',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `client_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '应用名称',
  `description` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `oauth_client_details` VALUES (6, 'wx-patient-minapp', '', '$2a$10$OzHMlDdZGSUHkuAoW/GxYOxs/94EO8M2V0vPU6p1Dlj2RDW99UU/i', 'wx-patient-minapp', 'all', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:8080/singleLogin', '', 3600, 28800, '{}', 'true', '2020-03-27 11:26:16', '2020-03-27 11:27:30', '微信小程序患者端', NULL);
INSERT INTO `oauth_client_details` VALUES (7, 'wx-doctor-minapp', '', '$2a$10$/4g5qhNHzHzrcXmWGEREUOxVR7aKwC1qYZjBtQy3hH4FtnxoiBX9C', 'wx-doctor-minapp', 'all', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:8080/singleLogin', '', 18000, 28800, '{}', 'true', '2020-03-27 11:27:20', '2020-03-27 11:27:20', '微信小程序医生端', NULL);
INSERT INTO `oauth_client_details` VALUES (11, 'micro-platform-pc', '', '$2a$10$GfQu0JpfKSP9Kfd0AQMSpOjwLXvX9NckWtMx0o5WFUsyhfEPAQBe.', 'micro-platform-pc', 'all', 'authorization_code,password_code,password,refresh_token,client_credentials', 'http://127.0.0.1:28001/singleLogin', '', 18000, 28800, '{}', 'true', '2020-07-10 08:11:19', '2020-07-10 08:11:19', '微服务平台PC端', NULL);
COMMIT;

-- ----------------------------
-- Table structure for oauth_dubbo_server
-- ----------------------------
DROP TABLE IF EXISTS `oauth_dubbo_server`;
CREATE TABLE `oauth_dubbo_server` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `client_id` varchar(32) DEFAULT NULL COMMENT '应用客户端标示',
  `service_interface` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '服务接口类',
  `service_description` varchar(128) DEFAULT NULL COMMENT '服务描述',
  `server_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '注册服务名称',
  `registry_center_url` varchar(128) DEFAULT NULL COMMENT '注册中心地址',
  `reversion` int DEFAULT NULL COMMENT '乐观锁',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='授权远程调用服务 1. oauth_client_details表与该表oauth_rpc_server是一对多的关系。2. oauth_rpc_server表存储被允许授权调用的rpc服务';

-- ----------------------------
-- Records of oauth_dubbo_server
-- ----------------------------
BEGIN;
INSERT INTO `oauth_dubbo_server` VALUES (1, 'micro-platform-pc', 'com.baimicro.central.user.IAppAuthService', '角色服务接口', 'user-platform-server', 'nacos://127.0.0.1:8848', 1, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_dubbo_server` VALUES (2, 'micro-platform-pc', 'com.baimicro.central.user.IAppPermService', '权限服务接口', 'user-platform-server', 'nacos://127.0.0.1:8848', 1, NULL, NULL, NULL, NULL);
INSERT INTO `oauth_dubbo_server` VALUES (3, 'micro-platform-pc', 'com.baimicro.central.user.IAppUserService', '用户服务接口', 'user-platform-server', 'nacos://127.0.0.1:8848', 1, NULL, NULL, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
