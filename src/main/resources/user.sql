CREATE TABLE `tb_user`
(
    `userId`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `nickname`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     DEFAULT NULL,
    `createdAt` timestamp                                                    NULL DEFAULT NULL,
    PRIMARY KEY (`userId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
