/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库
 Source Server Type    : MySQL
 Source Server Version : 50553
 Source Host           : localhost:3306
 Source Schema         : order01

 Target Server Type    : MySQL
 Target Server Version : 50553
 File Encoding         : 65001

 Date: 10/02/2020 13:13:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for block_content
-- ----------------------------
DROP TABLE IF EXISTS `block_content`;
CREATE TABLE `block_content`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `block_index` int(11) NOT NULL COMMENT '区块索引',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据内容',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `block_index`(`block_index`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '区块存储的内容' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
