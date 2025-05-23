-- MySQL Script generated by MySQL Workbench
-- Fri Feb 14 11:00:19 2025
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `software_db`;
USE `software_db`;

-- -----------------------------------------------------
-- Table `software_db`.`USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.user
(
    `id` VARCHAR(36) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `software_db`.`SOFTWARE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.software
(
    `id`          VARCHAR(36)                          NOT NULL,
    `name`        VARCHAR(150) CHARACTER SET 'utf8mb4' NOT NULL,
    `description` VARCHAR(255) CHARACTER SET 'utf8mb4' NULL,
    `created_at`  DATETIME                             NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME                             NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `USER_id`     VARCHAR(36)                          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_SOFTWARE_USER`
        FOREIGN KEY (`USER_id`)
            REFERENCES `software_db`.user (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_SOFTWARE_USER_idx` ON `software_db`.software (`USER_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`SOFTWARE_VERSION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.software_version
(
    `id`          VARCHAR(36)                          NOT NULL,
    `name`        VARCHAR(150) CHARACTER SET 'utf8mb4' NOT NULL,
    `description` VARCHAR(255) CHARACTER SET 'utf8mb4' NULL,
    `created_at`  DATETIME                             NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME                             NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `SOFTWARE_id` VARCHAR(36)                          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_SOFTWARE_SOFTWARE_VERSION1`
        FOREIGN KEY (`SOFTWARE_id`)
            REFERENCES `software_db`.software (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_SOFTWARE_SOFTWARE_VERSION_SOFTWARE_VERSION1_idx` ON `software_db`.software_version (`SOFTWARE_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`DOCUMENT_TYPE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.document_type
(
    `id`          VARCHAR(36)                          NOT NULL,
    `name`        VARCHAR(150) CHARACTER SET 'utf8mb4' NOT NULL,
    `description` VARCHAR(255) CHARACTER SET 'utf8mb4' NULL,
    `created_at`  DATETIME                             NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME                             NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `USER_id`     VARCHAR(36)                          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_DOCUMENT_TYPE_USER1`
        FOREIGN KEY (`USER_id`)
            REFERENCES `software_db`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_DOCUMENT_TYPE_USER1_idx` ON `software_db`.document_type (`USER_id` ASC) VISIBLE;

-- -----------------------------------------------------
-- Table `software_db`.`SOFTWARE_DOCUMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.software_document
(
    `id`          VARCHAR(36)                          NOT NULL,
    `name`        VARCHAR(150) CHARACTER SET 'utf8mb4' NOT NULL,
    `description` VARCHAR(255) CHARACTER SET 'utf8mb4' NULL,
    `created_at`  DATETIME                             NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME                             NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `TYPE_id`     VARCHAR(36)                          NOT NULL,
    `VERSION_id`  VARCHAR(36)                          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_SOFTWARE_DOCUMENT_DOCUMENT_TYPE_TYPE1`
        FOREIGN KEY (`TYPE_id`)
            REFERENCES `software_db`.`document_type` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_SOFTWARE_DOCUMENT_SOFTWARE_VERSION_VERSION1`
        FOREIGN KEY (`VERSION_id`)
            REFERENCES `software_db`.`software_version` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_SOFTWARE_DOCUMENT_DOCUMENT_TYPE_TYPE1_idx` ON `software_db`.software_document (`TYPE_id` ASC) VISIBLE;

CREATE INDEX `fk_SOFTWARE_DOCUMENT_SOFTWARE_VERSION_VERSION1_idx` ON `software_db`.software_document (`VERSION_id` ASC) VISIBLE;

-- -----------------------------------------------------
-- Table `software_db`.`MODULE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.module
(
    `id`                  VARCHAR(36)                          NOT NULL,
    `name`                VARCHAR(150) CHARACTER SET 'utf8mb4' NOT NULL,
    `description`         VARCHAR(255) CHARACTER SET 'utf8mb4' NULL,
    `created_at`          DATETIME                             NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          DATETIME                             NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `SOFTWARE_VERSION_id` VARCHAR(36)                          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_MODULE_SOFTWARE_VERSION_VERSION1`
        FOREIGN KEY (`SOFTWARE_VERSION_id`)
            REFERENCES `software_db`.software_version (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_MODULE_SOFTWARE_VERSION_VERSION1_idx` ON `software_db`.module (`SOFTWARE_VERSION_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`ATTACHMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.attachment
(
    `id` VARCHAR(36) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `software_db`.`MODULE_VERSION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.module_version
(
    `id`          VARCHAR(36)                          NOT NULL,
    `name`        VARCHAR(150) CHARACTER SET 'utf8mb4' NOT NULL,
    `description` VARCHAR(255) CHARACTER SET 'utf8mb4' NULL,
    `created_at`  DATETIME                             NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME                             NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `MODULE_id`   VARCHAR(36)                          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_MODULE_VERSION_MODULE_MODULE1`
        FOREIGN KEY (`MODULE_id`)
            REFERENCES `software_db`.module (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_MODULE_VERSION_MODULE_MODULE1_idx` ON `software_db`.module_version (`MODULE_id` ASC) INVISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`MODULE_DOCUMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.module_document
(
    `id`          VARCHAR(36)                          NOT NULL,
    `name`        VARCHAR(150) CHARACTER SET 'utf8mb4' NOT NULL,
    `description` VARCHAR(255) CHARACTER SET 'utf8mb4' NULL,
    `created_at`  DATETIME                             NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME                             NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `TYPE_id`     VARCHAR(36)                          NOT NULL,
    `VERSION_id`  VARCHAR(36)                          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_MODULE_DOCUMENT_DOCUMENT_TYPE_TYPE1`
        FOREIGN KEY (`TYPE_id`)
            REFERENCES `software_db`.`document_type` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_MODULE_DOCUMENT_MODULE_VERSION_VERSION1`
        FOREIGN KEY (`VERSION_id`)
            REFERENCES `software_db`.`module_version` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_MODULE_DOCUMENT_DOCUMENT_TYPE_TYPE1_idx` ON `software_db`.module_document (`TYPE_id` ASC) INVISIBLE;

CREATE INDEX `fk_MODULE_DOCUMENT_MODULE_VERSION_VERSION1_idx` ON `software_db`.module_document (`VERSION_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`CUSTOMER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.customer
(
    `id`              VARCHAR(36)  NOT NULL,
    `name`            VARCHAR(150) NOT NULL,
    `email`           VARCHAR(100) NOT NULL,
    `created_at`      DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `version`         BIGINT       NULL DEFAULT 0,
    `USER_CREATOR_id` VARCHAR(36)  NOT NULL COMMENT 'Who created this record',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_CUSTOMER_USER_USER1`
        FOREIGN KEY (`USER_CREATOR_id`)
            REFERENCES `software_db`.user (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_CUSTOMER_USER_USER1_idx` ON `software_db`.customer (`USER_CREATOR_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`DEPLOYMENT_PROCESS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.deployment_process
(
    `id`                  INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `status`              VARCHAR(50)  NOT NULL,
    `created_at`          DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `SOFTWARE_VERSION_id` VARCHAR(36)  NOT NULL,
    `CUSTOMER_id`         VARCHAR(36)  NOT NULL,
    `USER_CREATOR_id`     VARCHAR(36)  NOT NULL COMMENT 'Who created this record',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_DEPLOYMENT_PROCESS_SOFTWARE_VERSION_VERSION1`
        FOREIGN KEY (`SOFTWARE_VERSION_id`)
            REFERENCES `software_db`.software_version (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_DEPLOYMENT_PROCESS_CUSTOMER_CUSTOMER1`
        FOREIGN KEY (`CUSTOMER_id`)
            REFERENCES `software_db`.customer (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_DEPLOYMENT_PROCESS_USER1`
        FOREIGN KEY (`USER_CREATOR_id`)
            REFERENCES `software_db`.user (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_DEPLOYMENT_PROCESS_SOFTWARE_VERSION_VERSION1_idx` ON `software_db`.deployment_process (`SOFTWARE_VERSION_id` ASC) VISIBLE;

CREATE INDEX `fk_DEPLOYMENT_PROCESS_CUSTOMER_CUSTOMER1_idx` ON `software_db`.deployment_process (`CUSTOMER_id` ASC) VISIBLE;

CREATE INDEX `fk_DEPLOYMENT_PROCESS_USER1_idx` ON `software_db`.deployment_process (`USER_CREATOR_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`DEPLOYMENT_PHASE_TYPE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.deployment_phase_type
(
    `id`          VARCHAR(36)                          NOT NULL,
    `name`        VARCHAR(150) CHARACTER SET 'utf8mb4' NOT NULL,
    `description` VARCHAR(255) CHARACTER SET 'utf8mb4' NULL,
    `created_at`  DATETIME                             NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME                             NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `USER_id`     VARCHAR(36)                          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_DEPLOYMENT_PHASE_TYPE_USER_USER1`
        FOREIGN KEY (`USER_id`)
            REFERENCES `software_db`.user (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_DEPLOYMENT_PHASE_TYPE_USER_USER1_idx` ON `software_db`.deployment_phase_type (`USER_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`DEPLOYMENT_PHASE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.deployment_phase
(
    `id`                  VARCHAR(36)                          NOT NULL,
    `num_order`           SMALLINT UNSIGNED                    NOT NULL DEFAULT 0,
    `description`         VARCHAR(255) CHARACTER SET 'utf8mb4' NULL     DEFAULT NULL,
    `created_at`          DATETIME                             NULL     DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          DATETIME                             NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `planned_start_date`  DATE                                 NOT NULL,
    `planned_end_date`    DATE                                 NOT NULL,
    `actual_start_date`   DATE                                 NULL,
    `actual_end_date`     DATE                                 NULL,
    `is_done`             BIT(1)                               NOT NULL DEFAULT b'0',
    `TYPE_id`             VARCHAR(36)                          NOT NULL,
    `PROCESS_id`          INT UNSIGNED                         NOT NULL,
    `USER_last_update_id` VARCHAR(36)                          NULL COMMENT 'the last updating was done by this person.',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_DEPLOYMENT_PHASE_DEPLOYMENT_PROCESS_PROCESS1`
        FOREIGN KEY (`PROCESS_id`)
            REFERENCES `software_db`.`deployment_process` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_DEPLOYMENT_PHASE_DEPLOYMENT_PHASE_TYPE_TYPE1`
        FOREIGN KEY (`TYPE_id`)
            REFERENCES `software_db`.`deployment_phase_type` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_DEPLOYMENT_PHASE_USER1`
        FOREIGN KEY (`USER_last_update_id`)
            REFERENCES `software_db`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_DEPLOYMENT_PHASE_DEPLOYMENT_PROCESS_PROCESS1_idx` ON `software_db`.deployment_phase (`PROCESS_id` ASC) VISIBLE;

CREATE INDEX `fk_DEPLOYMENT_PHASE_DEPLOYMENT_PHASE_TYPE_TYPE1_idx` ON `software_db`.deployment_phase (`TYPE_id` ASC) VISIBLE;

CREATE INDEX `fk_DEPLOYMENT_PHASE_USER1_idx` ON `software_db`.deployment_phase (`USER_last_update_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`MODULE_DOCUMENT_has_ATTACHMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.module_document_has_attachment
(
    `DOCUMENT_id`   VARCHAR(36) NOT NULL,
    `ATTACHMENT_id` VARCHAR(36) NOT NULL,
    `created_at`    DATETIME    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`DOCUMENT_id`, `ATTACHMENT_id`),
    CONSTRAINT `fk_MODULE_DOCUMENT_has_ATTACHMENT_DOCUMENT1`
        FOREIGN KEY (`DOCUMENT_id`)
            REFERENCES `software_db`.module_document (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_MODULE_DOCUMENT_has_ATTACHMENT_ATTACHMENT1`
        FOREIGN KEY (`ATTACHMENT_id`)
            REFERENCES `software_db`.attachment (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_MODULE_DOCUMENT_has_ATTACHMENT_ATTACHMENT1_idx` ON `software_db`.module_document_has_attachment (`ATTACHMENT_id` ASC) VISIBLE;

CREATE INDEX `fk_MODULE_DOCUMENT_has_ATTACHMENT_DOCUMENT1_idx` ON `software_db`.module_document_has_attachment (`DOCUMENT_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`SOFTWARE_DOCUMENT_has_ATTACHMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.software_document_has_attachment
(
    `DOCUMENT_id`   VARCHAR(36) NOT NULL,
    `ATTACHMENT_id` VARCHAR(36) NOT NULL,
    `created_at`    DATETIME    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`DOCUMENT_id`, `ATTACHMENT_id`),
    CONSTRAINT `fk_SOFTWARE_DOCUMENT_has_ATTACHMENT_DOCUMENT1`
        FOREIGN KEY (`DOCUMENT_id`)
            REFERENCES `software_db`.software_document (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_SOFTWARE_DOCUMENT_has_ATTACHMENT_ATTACHMENT1`
        FOREIGN KEY (`ATTACHMENT_id`)
            REFERENCES `software_db`.attachment (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_SOFTWARE_DOCUMENT_has_ATTACHMENT_ATTACHMENT1_idx` ON `software_db`.software_document_has_attachment (`ATTACHMENT_id` ASC) VISIBLE;

CREATE INDEX `fk_SOFTWARE_DOCUMENT_has_ATTACHMENT_DOCUMENT1_idx` ON `software_db`.software_document_has_attachment (`DOCUMENT_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`DEPLOYMENT_PROCESS_has_USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.deployment_process_has_user
(
    `PROCESS_id` INT UNSIGNED NOT NULL,
    `USER_id`    VARCHAR(36)  NOT NULL,
    `created_at` DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`PROCESS_id`, `USER_id`),
    CONSTRAINT `fk_DEPLOYMENT_PROCESS_has_USER_PROCESS1`
        FOREIGN KEY (`PROCESS_id`)
            REFERENCES `software_db`.deployment_process (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_DEPLOYMENT_PROCESS_has_USER_USER1`
        FOREIGN KEY (`USER_id`)
            REFERENCES `software_db`.user (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_DEPLOYMENT_PROCESS_has_USER_USER1_idx` ON `software_db`.deployment_process_has_user (`USER_id` ASC) VISIBLE;

CREATE INDEX `fk_DEPLOYMENT_PROCESS_has_USER_PROCESS1_idx` ON `software_db`.deployment_process_has_user (`PROCESS_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`DEPLOYMENT_PROCESS_has_MODULE_VERSION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.deployment_process_has_module_version
(
    `PROCESS_id` INT UNSIGNED NOT NULL,
    `VERSION_id` VARCHAR(36)  NOT NULL,
    `created_at` DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`PROCESS_id`, `VERSION_id`),
    CONSTRAINT `fk_DEPLOYMENT_PROCESS_has_MODULE_VERSION_PROCESS1`
        FOREIGN KEY (`PROCESS_id`)
            REFERENCES `software_db`.deployment_process (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_DEPLOYMENT_PROCESS_has_MODULE_VERSION_VERSION1`
        FOREIGN KEY (`VERSION_id`)
            REFERENCES `software_db`.module_version (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_DEPLOYMENT_PROCESS_has_MODULE_VERSION_VERSION1_idx` ON `software_db`.deployment_process_has_module_version (`VERSION_id` ASC) VISIBLE;

CREATE INDEX `fk_DEPLOYMENT_PROCESS_has_MODULE_VERSION_PROCESS1_idx` ON `software_db`.deployment_process_has_module_version (`PROCESS_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`DEPLOYMENT_PHASE_has_ATTACHMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.deployment_phase_has_attachment
(
    `DEPLOYMENT_PHASE_id` VARCHAR(36) NOT NULL,
    `ATTACHMENT_id`       VARCHAR(36) NOT NULL,
    `created_at`          DATETIME    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`DEPLOYMENT_PHASE_id`, `ATTACHMENT_id`),
    CONSTRAINT `fk_DEPLOYMENT_PHASE_has_ATTACHMENT_DEPLOYMENT_PHASE1`
        FOREIGN KEY (`DEPLOYMENT_PHASE_id`)
            REFERENCES `software_db`.deployment_phase (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_DEPLOYMENT_PHASE_has_ATTACHMENT_ATTACHMENT1`
        FOREIGN KEY (`ATTACHMENT_id`)
            REFERENCES `software_db`.attachment (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_DEPLOYMENT_PHASE_has_ATTACHMENT_ATTACHMENT1_idx` ON `software_db`.deployment_phase_has_attachment (`ATTACHMENT_id` ASC) VISIBLE;

CREATE INDEX `fk_DEPLOYMENT_PHASE_has_ATTACHMENT_DEPLOYMENT_PHASE1_idx` ON `software_db`.deployment_phase_has_attachment (`DEPLOYMENT_PHASE_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`DEPLOYMENT_PHASE_has_USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.deployment_phase_has_user
(
    PHASE_id     VARCHAR(36) NOT NULL,
    `USER_id`    VARCHAR(36) NOT NULL,
    `created_at` DATETIME    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (PHASE_id, `USER_id`),
    CONSTRAINT `fk_DEPLOYMENT_PHASE_has_USER_DEPLOYMENT_PHASE1`
        FOREIGN KEY (PHASE_id)
            REFERENCES `software_db`.`deployment_phase` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_DEPLOYMENT_PHASE_has_USER_USER1`
        FOREIGN KEY (`USER_id`)
            REFERENCES `software_db`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_DEPLOYMENT_PHASE_has_USER_USER1_idx` ON `software_db`.deployment_phase_has_user (`USER_id` ASC) VISIBLE;

CREATE INDEX `fk_DEPLOYMENT_PHASE_has_USER_DEPLOYMENT_PHASE1_idx` ON `software_db`.deployment_phase_has_user (PHASE_id ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`UPDATE_PHASE_HISTORY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.update_phase_history
(
    `num_order`   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `USER_id`     VARCHAR(36)  NOT NULL,
    `PHASE_id`    VARCHAR(36)  NOT NULL,
    `description` VARCHAR(255) NULL,
    `is_done`     BIT(1)       NOT NULL DEFAULT b'0',
    created_at    DATETIME     NULL     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`num_order`, `USER_id`, `PHASE_id`),
    CONSTRAINT `fk_UPDATE_PHASE_HISTORY_USER1`
        FOREIGN KEY (`USER_id`)
            REFERENCES `software_db`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_UPDATE_PHASE_HISTORY_PHASE1`
        FOREIGN KEY (`PHASE_id`)
            REFERENCES `software_db`.`deployment_phase` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_UPDATE_PHASE_HISTORY_PHASE1_idx` ON `software_db`.update_phase_history (`PHASE_id` ASC) VISIBLE;

CREATE INDEX `fk_UPDATE_PHASE_HISTORY_USER1_idx` ON `software_db`.update_phase_history (`USER_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `software_db`.`EXPIRE_MAIL_TEMPLATE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.mail_template
(
    `id`         VARCHAR(36)  NOT NULL,
    `subject`    VARCHAR(150) NOT NULL,
    `content`    BLOB         NOT NULL,
    `created_at` DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `type`       VARCHAR(45)  NOT NULL,
    `USER_id`    VARCHAR(36)  NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT fk_MAIL_TEMPLATE_USER1
        FOREIGN KEY (`USER_id`)
            REFERENCES `software_db`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX fk_MAIL_TEMPLATE_USER1_idx ON `software_db`.mail_template (`USER_id` ASC) VISIBLE;

-- -----------------------------------------------------
-- Table `software_db`.`SOFTWARE_LICENSE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_db`.software_license
(
    `id`                        VARCHAR(36)                          NOT NULL,
    `description`               VARCHAR(255) CHARACTER SET 'utf8mb4' NULL,
    `start_time`                DATETIME                             NOT NULL,
    `end_time`                  DATETIME                             NOT NULL,
    `is_expire_alert_done`      BIT(1)                               NOT NULL DEFAULT b'0',
    `expire_alert_interval_day` SMALLINT UNSIGNED                    NOT NULL DEFAULT 15,
    `created_at`                DATETIME                             NULL     DEFAULT CURRENT_TIMESTAMP,
    `updated_at`                DATETIME                             NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `DEPLOYMENT_PROCESS_id`     INT UNSIGNED                         NOT NULL,
    `USER_CREATOR_id`           VARCHAR(36)                          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_SOFTWARE_LICENSE_DEPLOYMENT_PROCESS1`
        FOREIGN KEY (`DEPLOYMENT_PROCESS_id`)
            REFERENCES `software_db`.`deployment_process` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_SOFTWARE_LICENSE_USER1`
        FOREIGN KEY (`USER_CREATOR_id`)
            REFERENCES `software_db`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE INDEX `fk_SOFTWARE_LICENSE_DEPLOYMENT_PROCESS1_idx` ON `software_db`.software_license (`DEPLOYMENT_PROCESS_id` ASC) VISIBLE;

CREATE INDEX `fk_SOFTWARE_LICENSE_USER1_idx` ON `software_db`.software_license (`USER_CREATOR_id` ASC) VISIBLE;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
