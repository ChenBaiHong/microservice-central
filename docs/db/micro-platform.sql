/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库01
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:33306
 Source Schema         : micro-platform

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 26/07/2020 18:49:33
*/
CREATE DATABASE IF NOT EXISTS `micro-platform` DEFAULT CHARACTER SET = utf8mb4;
Use `micro-platform`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for platf_permission
-- ----------------------------
DROP TABLE IF EXISTS `platf_permission`;
CREATE TABLE `platf_permission` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent_id` int DEFAULT NULL COMMENT '父id',
  `name` varchar(100) DEFAULT NULL COMMENT '菜单标题',
  `url` varchar(255) DEFAULT NULL COMMENT '路径',
  `component` varchar(255) DEFAULT NULL COMMENT '组件',
  `component_name` varchar(100) DEFAULT NULL COMMENT '组件名字',
  `redirect` varchar(255) DEFAULT NULL COMMENT '一级菜单跳转地址',
  `menu_type` int DEFAULT NULL COMMENT '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
  `perms` varchar(255) DEFAULT NULL COMMENT '菜单权限编码',
  `perms_type` int DEFAULT '0' COMMENT '权限策略1显示2禁用',
  `sort_no` int DEFAULT NULL COMMENT '菜单排序',
  `always_show` tinyint(1) DEFAULT NULL COMMENT '聚合子路由: 1是0否',
  `icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `is_route` tinyint(1) DEFAULT '1' COMMENT '是否路由菜单: 0:不是  1:是（默认值1）',
  `is_leaf` tinyint(1) DEFAULT NULL COMMENT '是否叶子节点:    1:是   0:不是',
  `keep_alive` tinyint(1) DEFAULT NULL COMMENT '是否缓存该页面:    1:是   0:不是',
  `hidden` int DEFAULT '0' COMMENT '是否隐藏路由: 0否,1是',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` int DEFAULT '0' COMMENT '删除状态 0正常 1已删除',
  `rule_flag` int DEFAULT '0' COMMENT '是否添加数据权限1是0否',
  `status` varchar(2) DEFAULT NULL COMMENT '按钮权限状态(0无效1有效)',
  `internal_or_external` tinyint(1) DEFAULT NULL COMMENT '外链菜单打开方式 0/内部打开 1/外部打开',
  `tenant_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '多租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_prem_pid` (`parent_id`) USING BTREE,
  KEY `index_prem_is_route` (`is_route`) USING BTREE,
  KEY `index_prem_is_leaf` (`is_leaf`) USING BTREE,
  KEY `index_prem_sort_no` (`sort_no`) USING BTREE,
  KEY `index_prem_del_flag` (`del_flag`) USING BTREE,
  KEY `index_menu_type` (`menu_type`) USING BTREE,
  KEY `index_menu_hidden` (`hidden`) USING BTREE,
  KEY `index_menu_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';

-- ----------------------------
-- Records of platf_permission
-- ----------------------------
BEGIN;
INSERT INTO `platf_permission` VALUES (1, NULL, '系统管理', '/system', 'layouts/RouteView', NULL, NULL, 0, NULL, 1, 1, 0, 'setting', 1, 0, 0, 0, NULL, 'admin', '2020-05-04 10:31:26', 'admin', '2020-07-10 17:55:24', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (2, 1, '菜单管理', '/system/permission', 'modules/system/perm-manage/PermissionManage', NULL, NULL, 1, NULL, 1, 1, 0, 'bars', 1, 1, 0, 0, NULL, 'admin', '2020-05-04 10:37:19', 'admin', '2020-07-10 17:25:02', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (4, 1, '数据字典', '/system/dict', 'modules/system/dict-manage/DictManage', NULL, NULL, 1, NULL, NULL, 5, 0, 'read', 1, 1, 0, 0, NULL, NULL, '2018-12-28 13:54:43', 'admin', '2020-07-11 09:08:42', 0, 0, NULL, 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (6, 1, '角色管理', '/system/roleUser', 'modules/system/role-manage/RoleManage', NULL, NULL, 1, NULL, NULL, 3, 0, 'team', 1, 0, 0, 0, NULL, 'admin', '2019-04-17 15:13:56', 'admin', '2020-07-10 17:49:44', 0, 0, NULL, 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (7, 1, '用户管理', '/system/user', 'modules/system/user-manage/UserMange', NULL, NULL, 1, NULL, NULL, 2, 0, 'user', 1, 0, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2020-07-10 17:49:59', 0, 0, NULL, 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (16, 7, '手机号禁用', NULL, NULL, NULL, NULL, 2, 'user:form:phone', 2, 1, 0, NULL, 0, 1, NULL, 0, NULL, 'admin', '2019-05-11 17:19:30', 'admin', '2019-05-11 18:00:22', 0, 0, '1', NULL, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (17, 7, '添加用户按钮', NULL, NULL, NULL, NULL, 2, 'user:add', 1, 1, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-03-16 11:20:33', 'admin', '2019-05-17 18:31:25', 0, 0, '1', NULL, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (40, 6, '删除权限', NULL, NULL, NULL, NULL, 2, 'role:delete', 1, 1, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-05-08 09:20:29', NULL, NULL, 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (42, 1, '应用管理', '/system/app', 'modules/system/app-manage/AppManage', NULL, NULL, 1, NULL, 1, 4, 0, 'appstore', 1, 1, 0, 0, NULL, NULL, '2020-07-10 17:35:52', NULL, '2020-07-10 17:50:16', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (43, 1, 'Token 管理', '/system/token', 'modules/system/token-manage/RedisTokenManage', NULL, NULL, 1, NULL, 1, 5, 0, 'safety-certificate', 1, 1, 0, 0, NULL, NULL, '2020-07-10 17:49:17', NULL, '2020-07-12 18:01:35', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (44, NULL, '管理中心', '/central', 'layouts/RouteView', NULL, NULL, 0, NULL, 1, 2, 0, 'setting', 1, 0, 0, 0, NULL, NULL, '2020-07-10 18:02:01', NULL, '2020-07-11 18:49:42', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (45, 44, '配置中心', '/server/setting', 'server/setting', NULL, NULL, 1, NULL, 1, 1, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-10 18:06:25', NULL, NULL, 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (47, 44, '文件中心', '/server/file', 'server/file', NULL, NULL, 1, NULL, 1, 2, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-10 18:07:52', NULL, NULL, 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (48, NULL, '搜索管理', '/search', 'layouts/RouteView', NULL, NULL, 0, NULL, 1, 3, 0, 'search', 1, 1, 0, 0, NULL, NULL, '2020-07-10 18:12:14', NULL, '2020-07-10 18:18:55', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (49, NULL, '监控管理', '/monitor', 'layouts/RouteView', NULL, NULL, 0, NULL, 1, 4, 0, 'eye', 1, 0, 0, 0, NULL, NULL, '2020-07-10 18:13:31', NULL, '2020-07-10 18:19:01', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (50, 1, '应用服务', '/oauth/service', 'modules/system/app-service/AppServiceManage', NULL, NULL, 1, NULL, 1, 6, 0, 'block', 1, 1, 0, 0, NULL, NULL, '2020-07-10 18:16:43', NULL, '2020-07-12 09:30:48', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (51, NULL, '任务管理', '/quartz/job', 'layouts/RouteView', NULL, NULL, 0, NULL, 1, 5, 0, 'deployment-unit', 1, 1, 0, 0, NULL, NULL, '2020-07-10 18:32:46', NULL, '2020-07-16 21:12:40', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (52, 49, '性能监控', '/monitor/nature', 'layouts/RouteView', NULL, NULL, 1, NULL, 1, 1, 0, NULL, 1, 0, 0, 0, NULL, NULL, '2020-07-13 08:14:21', NULL, '2020-07-13 08:17:17', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (53, 52, 'JVM信息', '/monitor/nature/jvm', 'modules/monitor/JvmInfo', NULL, NULL, 1, NULL, 1, 1, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-13 08:15:41', NULL, NULL, 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (54, 52, 'Tomcat信息', '/monitor/nature/tomcat', 'modules/monitor/TomcatInfo', NULL, NULL, 1, NULL, 1, 2, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-13 08:19:21', NULL, '2020-07-13 08:19:51', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (55, 52, 'Redis信息', '/monitor/nature/redis', 'modules/monitor/RedisInfo', NULL, NULL, 1, NULL, 1, 3, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-13 08:21:07', NULL, NULL, 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (56, 52, '服务器信息', '/monitor/nature/system', 'modules/monitor/SystemInfo', NULL, NULL, 1, NULL, 1, 4, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-13 08:22:32', NULL, NULL, 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (57, 49, 'APM监控', '/monitor/apm', 'layouts/RouteView', NULL, NULL, 1, NULL, 1, 3, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-13 08:29:46', NULL, '2020-07-13 08:31:10', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (58, 49, '应用吞吐量监控', '/monitor/appCapacity', 'layouts/RouteView', NULL, NULL, 1, NULL, 1, 2, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-13 08:30:42', NULL, '2020-07-13 08:32:03', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (59, 49, '服务器监控', '/monitor/system', 'layouts/RouteView', NULL, NULL, 1, NULL, 1, 4, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-13 08:32:50', NULL, '2020-07-15 18:05:26', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (60, 7, '编辑用户按钮', NULL, NULL, NULL, NULL, 2, 'user:edit', 1, 1, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-15 18:06:44', NULL, '2020-07-15 18:07:14', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (61, 7, '删除用户按钮', '', NULL, NULL, NULL, 2, 'user:delete', 1, 1, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-15 18:07:58', NULL, '2020-07-15 20:29:28', 0, 0, '1', 0, 'micro-platform-pc');
INSERT INTO `platf_permission` VALUES (62, 52, '请求追踪', '/monitor/nature/httptrace', 'modules/monitor/HttpTrace', NULL, NULL, 1, NULL, 1, 5, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2020-07-17 11:59:07', NULL, NULL, 0, 0, '1', 0, 'micro-platform-pc');
COMMIT;

-- ----------------------------
-- Table structure for platf_role
-- ----------------------------
DROP TABLE IF EXISTS `platf_role`;
CREATE TABLE `platf_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_name` varchar(200) DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(100) NOT NULL COMMENT '角色编码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '多租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_sys_role_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of platf_role
-- ----------------------------
BEGIN;
INSERT INTO `platf_role` VALUES (1, '超级管理员', 'admin', '系统超级管理员', 'admin', '2020-05-04 19:39:38', 'admin', '2020-05-07 16:42:27', 'micro-platform-pc');
INSERT INTO `platf_role` VALUES (4, '人工客服人员', 'ManualCustomerService', 'AI 导诊人工客服人员', NULL, '2020-05-07 16:42:13', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for platf_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `platf_role_permission`;
CREATE TABLE `platf_role_permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT NULL COMMENT '角色id',
  `permission_id` int DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_group_role_per_id` (`role_id`,`permission_id`) USING BTREE,
  KEY `index_group_role_id` (`role_id`) USING BTREE,
  KEY `index_group_per_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色权限表';

-- ----------------------------
-- Records of platf_role_permission
-- ----------------------------
BEGIN;
INSERT INTO `platf_role_permission` VALUES (1, 1, 1, NULL);
INSERT INTO `platf_role_permission` VALUES (2, 1, 2, NULL);
INSERT INTO `platf_role_permission` VALUES (6, 1, 6, NULL);
INSERT INTO `platf_role_permission` VALUES (7, 1, 7, NULL);
INSERT INTO `platf_role_permission` VALUES (16, 1, 16, NULL);
INSERT INTO `platf_role_permission` VALUES (17, 1, 17, NULL);
INSERT INTO `platf_role_permission` VALUES (26, 1, 42, NULL);
INSERT INTO `platf_role_permission` VALUES (27, 1, 43, NULL);
INSERT INTO `platf_role_permission` VALUES (29, 1, 45, NULL);
INSERT INTO `platf_role_permission` VALUES (30, 1, 44, NULL);
INSERT INTO `platf_role_permission` VALUES (31, 1, 47, NULL);
INSERT INTO `platf_role_permission` VALUES (32, 1, 48, NULL);
INSERT INTO `platf_role_permission` VALUES (33, 1, 49, NULL);
INSERT INTO `platf_role_permission` VALUES (34, 1, 50, NULL);
INSERT INTO `platf_role_permission` VALUES (35, 1, 51, NULL);
INSERT INTO `platf_role_permission` VALUES (36, 1, 4, NULL);
INSERT INTO `platf_role_permission` VALUES (38, 4, 42, NULL);
INSERT INTO `platf_role_permission` VALUES (39, 1, 52, NULL);
INSERT INTO `platf_role_permission` VALUES (40, 1, 53, NULL);
INSERT INTO `platf_role_permission` VALUES (41, 1, 54, NULL);
INSERT INTO `platf_role_permission` VALUES (42, 1, 55, NULL);
INSERT INTO `platf_role_permission` VALUES (43, 1, 56, NULL);
INSERT INTO `platf_role_permission` VALUES (44, 1, 58, NULL);
INSERT INTO `platf_role_permission` VALUES (45, 1, 57, NULL);
INSERT INTO `platf_role_permission` VALUES (46, 1, 59, NULL);
INSERT INTO `platf_role_permission` VALUES (47, 4, 43, NULL);
INSERT INTO `platf_role_permission` VALUES (48, 4, 4, NULL);
INSERT INTO `platf_role_permission` VALUES (49, 4, 50, NULL);
INSERT INTO `platf_role_permission` VALUES (50, 4, 49, NULL);
INSERT INTO `platf_role_permission` VALUES (51, 4, 52, NULL);
INSERT INTO `platf_role_permission` VALUES (52, 4, 53, NULL);
INSERT INTO `platf_role_permission` VALUES (54, 4, 55, NULL);
INSERT INTO `platf_role_permission` VALUES (55, 4, 56, NULL);
INSERT INTO `platf_role_permission` VALUES (56, 4, 58, NULL);
INSERT INTO `platf_role_permission` VALUES (57, 4, 57, NULL);
INSERT INTO `platf_role_permission` VALUES (58, 4, 59, NULL);
INSERT INTO `platf_role_permission` VALUES (59, 4, 1, NULL);
INSERT INTO `platf_role_permission` VALUES (60, 1, 61, NULL);
INSERT INTO `platf_role_permission` VALUES (61, 1, 60, NULL);
INSERT INTO `platf_role_permission` VALUES (62, 4, 6, NULL);
INSERT INTO `platf_role_permission` VALUES (63, 4, 2, NULL);
INSERT INTO `platf_role_permission` VALUES (64, 4, 48, NULL);
INSERT INTO `platf_role_permission` VALUES (65, 4, 47, NULL);
INSERT INTO `platf_role_permission` VALUES (66, 4, 45, NULL);
INSERT INTO `platf_role_permission` VALUES (67, 4, 44, NULL);
INSERT INTO `platf_role_permission` VALUES (68, 4, 7, NULL);
INSERT INTO `platf_role_permission` VALUES (69, 1, 62, NULL);
INSERT INTO `platf_role_permission` VALUES (70, 4, 62, NULL);
COMMIT;

-- ----------------------------
-- Table structure for platf_user
-- ----------------------------
DROP TABLE IF EXISTS `platf_user`;
CREATE TABLE `platf_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(100) DEFAULT NULL COMMENT '登录账号',
  `realname` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `sex` varchar(10) DEFAULT NULL COMMENT '性别(0-默认未知,1-男,2-女)',
  `email` varchar(45) DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) DEFAULT NULL COMMENT '电话',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '身份状态(1-正常,0-冻结)',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除状态(0-正常,1-已删除)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant_id` varchar(100) DEFAULT NULL COMMENT '多租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_user_name` (`username`) USING BTREE,
  KEY `index_user_status` (`enabled`) USING BTREE,
  KEY `index_user_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of platf_user
-- ----------------------------
BEGIN;
INSERT INTO `platf_user` VALUES (1, 'admin', '吾乃二战神', '$2a$10$5SIcPeBmw9XOc1BVin3Y3uH5/.XjFLPNGqnPv1S8wil5vD0A/CA4G', NULL, '男', 'baiHoo.chen@hotmail.com', '16602895995', 1, 0, 'admin', '2020-05-04 19:35:18', 'admin', '2020-07-26 17:52:43', 'micro-platform-pc');
INSERT INTO `platf_user` VALUES (7, 'libai', '李白', '$2a$10$3JKMV02t1/UyYS5gtkA57egw9iuCViWGU55RtUSwP1NdaOHOqaR4K', '[]', '男', 'libai@baike.com', '13678005445', 1, 0, NULL, '2020-07-11 15:17:20', NULL, '2020-07-26 15:36:02', 'micro-platform-pc');
INSERT INTO `platf_user` VALUES (12, 'ArtLangdon', '亚特.兰登', '$2a$10$OOK0QFTsMatIV5Ga3zO4COzvyFby3KQJMPgKmFmM9XGa.Fi7gt7eO', '[]', '男', 'chen.baihoo@gmail.com', '13678005440', 1, 0, NULL, '2020-07-14 10:01:34', NULL, '2020-07-26 12:13:54', 'micro-platform-pc');
COMMIT;

-- ----------------------------
-- Table structure for platf_user_role
-- ----------------------------
DROP TABLE IF EXISTS `platf_user_role`;
CREATE TABLE `platf_user_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `role_id` int DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index2_groupuu_user_id` (`user_id`) USING BTREE,
  KEY `index2_groupuu_ole_id` (`role_id`) USING BTREE,
  KEY `index2_groupuu_useridandroleid` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
-- Records of platf_user_role
-- ----------------------------
BEGIN;
INSERT INTO `platf_user_role` VALUES (57, 1, 1);
INSERT INTO `platf_user_role` VALUES (5, 3, 4);
INSERT INTO `platf_user_role` VALUES (3, 4, 4);
INSERT INTO `platf_user_role` VALUES (6, 5, 1);
INSERT INTO `platf_user_role` VALUES (20, 6, 1);
INSERT INTO `platf_user_role` VALUES (55, 7, 4);
INSERT INTO `platf_user_role` VALUES (16, 8, 1);
INSERT INTO `platf_user_role` VALUES (17, 9, 1);
INSERT INTO `platf_user_role` VALUES (18, 10, 4);
INSERT INTO `platf_user_role` VALUES (19, 11, 4);
INSERT INTO `platf_user_role` VALUES (37, 12, 4);
INSERT INTO `platf_user_role` VALUES (22, 13, 1);
INSERT INTO `platf_user_role` VALUES (23, 14, 4);
INSERT INTO `platf_user_role` VALUES (30, 15, 4);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dict_name` varchar(100) DEFAULT NULL COMMENT '字典名称',
  `dict_code` varchar(100) DEFAULT NULL COMMENT '字典编码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `del_flag` int DEFAULT '0' COMMENT '删除状态',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `type` int(1) unsigned zerofill DEFAULT '0' COMMENT '字典类型0为string,1为number',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `indextable_dict_code` (`dict_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES (1, '授权方式', 'authorization_mode', '8 种 oauth 授权方式', 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES (2, '有效状态', 'valid_status', '是和否 为判断策略', 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES (3, '表单权限策略', 'global_perms_type', '', 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES (4, '是否布尔判断', 'true_or_false', '', 0, NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dict_id` int DEFAULT NULL COMMENT '字典id',
  `item_text` varchar(100) DEFAULT NULL COMMENT '字典项文本',
  `item_value` varchar(100) DEFAULT NULL COMMENT '字典项值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort_order` int DEFAULT NULL COMMENT '排序',
  `status` int DEFAULT NULL COMMENT '状态（1启用 0不启用）',
  `create_by` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_table_dict_id` (`dict_id`) USING BTREE,
  KEY `index_table_sort_order` (`sort_order`) USING BTREE,
  KEY `index_table_dict_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_item` VALUES (1, 1, '授权码模式', 'authorization_code', '8种 oauth 授权方式 中的 授权码模式', 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (2, 1, '密码模式', 'password', '8种 oauth 授权方式 中的 密码模式', 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (3, 1, '客户端模式', 'client_credentials', '8种 oauth 授权方式 中的 客户端模式', 3, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (4, 1, '刷新Token模式', 'refresh_token', '8种 oauth 授权方式 中的 刷新Token模式', 4, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (6, 2, '有效', '1', '', 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (7, 2, '无效', '0', '', 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (8, 3, '可见(未授权不可见)', '1', '', 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (9, 3, '可编辑(未授权禁用)', '2', '', 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (10, 4, '是', 'true', '', 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (11, 4, '否', 'false', '', 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (13, 1, '简化模式', 'implicit', '8种 oauth 授权方式 中的 简化模式', 5, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (14, 1, '密码验证模式', 'password_code', '8种 oauth 授权方式 中的 密码验证模式', 6, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (15, 1, 'openId模式', 'openId', '8种 oauth 授权方式 中的 openId模式', 7, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (16, 1, '手机号密码模式', 'mobile_password', '8种 oauth 授权方式 中的 手机号密码模式', 8, 1, NULL, NULL, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
