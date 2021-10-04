/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_root_123456
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost
 Source Database       : appuser

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : utf-8

 Date: 09/30/2021 16:30:16 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `boot_config`
-- ----------------------------
DROP TABLE IF EXISTS `boot_config`;
CREATE TABLE `boot_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `config_type` varchar(255) DEFAULT NULL,
  `config_value` varchar(2550) DEFAULT NULL,
  `type_des` varchar(255) DEFAULT NULL,
  `value_des` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `flag` varchar(2) DEFAULT '01',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='配置项';

-- ----------------------------
--  Records of `boot_config`
-- ----------------------------
BEGIN;
INSERT INTO `boot_config` VALUES ('1', 'token_valid', '30', 'token失效时间', 'token有效期', '2021-09-29 00:26:25', '2021-09-29 00:26:25', '01'), ('2', 'user_pwd_md5_enc', 'true', '密码md5加密', null, '2021-09-29 00:37:09', '2021-09-29 00:37:09', '02'), ('3', 'cache', 'Redis', '缓存', 'Redis缓存', '2021-09-29 02:49:35', '2021-09-30 05:10:11', '02'), ('4', 'cache', 'CaffeineCache', '缓存', 'CaffeineCache本地缓存', '2021-09-29 02:49:59', '2021-09-30 05:11:14', '01'), ('6', 'url', '/config', '页面', '配置页面', '2021-09-30 08:16:57', '2021-09-30 08:16:57', '01'), ('7', 'url', '/useInfoList', '页面', '用户信息列表页面', '2021-09-30 08:20:29', '2021-09-30 08:20:29', '01'), ('8', 'url', '/helloword', '页面', '欢迎页面', '2021-09-30 08:27:33', '2021-09-30 08:27:33', '01');
COMMIT;

-- ----------------------------
--  Table structure for `boot_role`
-- ----------------------------
DROP TABLE IF EXISTS `boot_role`;
CREATE TABLE `boot_role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `url_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限';

-- ----------------------------
--  Table structure for `boot_user`
-- ----------------------------
DROP TABLE IF EXISTS `boot_user`;
CREATE TABLE `boot_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `address` varchar(512) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `flag` varchar(16) NOT NULL DEFAULT '01',
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2130 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
--  Records of `boot_user`
-- ----------------------------
BEGIN;
INSERT INTO `boot_user` VALUES ('2101', '张三we23432', '12', '南京123123131南京123123131南京123123131南京123123131南京123123131南京123123131南京123123131南京123123131南京123123131南京123123131南京123123131南京123123131南京123123131', '2021-09-27 00:00:00', '2021-09-27 12:40:26', '01', null), ('2104', '第三方士大夫', null, '222', '2021-09-07 00:00:00', '2021-09-27 12:40:26', '02', null), ('2105', '123', null, '123', '2021-09-01 00:00:00', '2021-09-27 12:40:27', '02', null), ('2106', '12312', null, '123123', '2021-09-01 00:00:00', '2021-09-27 11:58:02', '02', null), ('2107', '123', null, '123', '2021-09-04 00:00:00', '2021-09-27 12:40:27', '02', null), ('2109', 'hahah ', null, '213', '2021-10-05 00:00:00', '2021-09-27 12:40:27', '02', null), ('2110', '777', null, '777', '2021-09-06 00:00:00', '2021-09-27 12:40:27', '02', null), ('2111', '8888', null, '8888', '2021-09-29 00:00:00', '2021-09-27 12:40:27', '02', null), ('2112', '9999', null, '99999', '2021-06-15 00:00:00', '2021-09-27 09:15:02', '01', null), ('2113', '101010', null, 'ttgfhfgh', '2021-04-15 00:00:00', '2021-09-27 09:15:03', '01', null), ('2114', '123213', null, '1112321', '2021-09-22 00:00:00', '2021-09-28 12:44:40', '01', 'e2fc714c4727ee9395f324cd2e7f331f'), ('2115', 'aaaaaaa111333', null, '2312321aaa', '2021-09-22 00:00:00', '2021-09-28 12:31:25', '01', null), ('2116', '123', null, '123', '2021-09-27 08:14:16', '2021-09-27 09:15:03', '01', '202cb962ac59075b964b07152d234b70'), ('2117', '123123', null, '123213', '2021-09-22 00:00:00', '2021-09-27 09:15:03', '01', null), ('2118', '123', null, '123123', '2021-09-27 09:14:59', '2021-09-27 09:15:03', '02', null), ('2119', '123', null, '123', '2021-09-27 00:00:00', '2021-09-27 09:15:12', '02', null), ('2120', '123123', null, '123213', '2021-09-27 09:15:21', '2021-09-27 09:15:32', '01', null), ('2121', 'hahahah', null, '5555555555', '2021-09-06 00:00:00', '2021-09-27 11:39:20', '01', null), ('2122', '222222', null, '2345676543', '2021-09-15 00:00:00', '2021-09-27 11:39:27', '01', null), ('2123', '33333', null, '3333333', '2021-09-27 00:00:00', '2021-09-28 12:04:03', '02', null), ('2124', '奥术大师所', null, '阿斯顿撒多', '2021-09-06 00:00:00', '2021-09-28 07:57:54', '01', null), ('2125', '12312312', null, '12321312', '2021-09-15 00:00:00', '2021-09-28 07:57:45', '02', null), ('2126', '00000', null, '00000', '2021-09-08 00:00:00', '2021-09-28 07:57:42', '02', null), ('2127', 'hahahha2222123', null, 'hahahah6666', '2021-09-07 00:00:00', '2021-09-27 13:03:11', '02', null), ('2128', '11', '444', null, '2020-09-08 23:09:07', '2021-09-29 08:02:18', '01', null), ('2129', '1测试', '123', null, '2021-09-08 00:00:00', '2021-09-29 08:03:03', '01', null);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
