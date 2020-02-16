SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for node_info
-- ----------------------------
DROP TABLE IF EXISTS `node_info`;
CREATE TABLE `node_info`
(
    `id`            int(11)                                                 NOT NULL AUTO_INCREMENT COMMENT '主键',
    `node_id`       int(11)                                                 NULL DEFAULT NULL COMMENT '节点ID（OrderId Or PeerId）',
    `name`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点名称',
    `address`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网络地址',
    `type`          int(11)                                                 NULL DEFAULT NULL COMMENT '节点类型',
    `public_key`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点的公钥',
    `register_time` datetime                                                NULL DEFAULT NULL COMMENT '注册时间',
    `status`        int(11)                                                 NULL DEFAULT NULL COMMENT '节点状态',
    `block_index`   int(11)                                                 NULL DEFAULT NULL COMMENT '节点保存的区块高度',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '节点信息表'
  ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
