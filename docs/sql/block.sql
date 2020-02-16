SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for block
-- ----------------------------
DROP TABLE IF EXISTS `block`;
CREATE TABLE `block`
(
    `id`              int(11)                                                NOT NULL AUTO_INCREMENT COMMENT '主键',
    `block_index`     int(11)                                                NULL DEFAULT NULL COMMENT '区块索引',
    `hash_value`      varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区块索引值',
    `prev_hash_value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上一个区块的索引值',
    `create_time`     datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '区块存储结构'
  ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
