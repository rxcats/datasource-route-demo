# create database
CREATE DATABASE `commondb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE `userdb0` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE `userdb1` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# commondb
USE `commondb`;
CREATE TABLE `tb_id`
(
    `idType`  varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
    `idValue` bigint(20) unsigned                    NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`idType`, `idValue`),
    KEY `idValue` (`idValue`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
;

CREATE TABLE `tb_common_user`
(
    `userId`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `nickname`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     DEFAULT NULL,
    `shardNo`   int(11)                                                           DEFAULT '0',
    `createdAt` timestamp                                                    NULL DEFAULT NULL,
    PRIMARY KEY (`userId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
;

# userdb0
USE `userdb0`;
CREATE TABLE `tb_user`
(
    `userId`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `nickname`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     DEFAULT NULL,
    `createdAt` timestamp                                                    NULL DEFAULT NULL,
    PRIMARY KEY (`userId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
;

# userdb1
USE `userdb1`;
CREATE TABLE `tb_user`
(
    `userId`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `nickname`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     DEFAULT NULL,
    `createdAt` timestamp                                                    NULL DEFAULT NULL,
    PRIMARY KEY (`userId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
;
