SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for block_content
-- ----------------------------
DROP TABLE IF EXISTS `block_content`;
CREATE TABLE `block_content`
(
    `id`          int(11)                                                 NOT NULL AUTO_INCREMENT COMMENT '主键',
    `block_index` int(11)                                                 NOT NULL COMMENT '区块索引',
    `content`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据内容',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `block_index` (`block_index`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '区块存储的内容'
  ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
