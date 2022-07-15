-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema my_stocks_db
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `my_stocks_db` ;

-- -----------------------------------------------------
-- Schema my_stocks_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `my_stocks_db` DEFAULT CHARACTER SET utf8mb4 ;
USE `my_stocks_db` ;

-- -----------------------------------------------------
-- Table `my_stocks_db`.`broker`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `my_stocks_db`.`broker` ;

CREATE TABLE IF NOT EXISTS `my_stocks_db`.`broker` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `broker_short_name` VARCHAR(50) NOT NULL,
  `broker_name` VARCHAR(200) NOT NULL,
  `broker_cnpj` VARCHAR(18) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `broker_short_name_UNIQUE` (`broker_short_name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `my_stocks_db`.`broker_invoice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `my_stocks_db`.`broker_invoice` ;

CREATE TABLE IF NOT EXISTS `my_stocks_db`.`broker_invoice` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `invoice_number` INT(11) NOT NULL,
  `invoice_date` DATE NULL DEFAULT NULL,
  `total_sold` DECIMAL(10,2) NULL DEFAULT '0.00',
  `total_bought` DECIMAL(10,2) NULL DEFAULT '0.00',
  `total_liquidation_tax` DECIMAL(10,2) NULL DEFAULT '0.00',
  `total_fees` DECIMAL(10,2) NULL DEFAULT '0.00',
  `total_tax` DECIMAL(10,2) NULL DEFAULT '0.00',
  `total_brokerage` DECIMAL(10,2) NULL DEFAULT '0.00',
  `broker_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_broker_invoice_broker1_idx` (`broker_id` ASC),
  CONSTRAINT `fk_broker_invoice_broker1`
    FOREIGN KEY (`broker_id`)
    REFERENCES `my_stocks_db`.`broker` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `my_stocks_db`.`finantial_movement_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `my_stocks_db`.`finantial_movement_type` ;

CREATE TABLE IF NOT EXISTS `my_stocks_db`.`finantial_movement_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(200) NOT NULL,
  `movement_type` VARCHAR(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `my_stocks_db`.`stock_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `my_stocks_db`.`stock_type` ;

CREATE TABLE IF NOT EXISTS `my_stocks_db`.`stock_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `my_stocks_db`.`stock`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `my_stocks_db`.`stock` ;

CREATE TABLE IF NOT EXISTS `my_stocks_db`.`stock` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_api` INT(11) NULL DEFAULT NULL,
  `code` VARCHAR(10) NOT NULL,
  `company_cnpj` VARCHAR(18) NOT NULL,
  `company_name` VARCHAR(200) NOT NULL,
  `holder_company_cnpj` VARCHAR(18) NULL DEFAULT NULL,
  `holder_company_name` VARCHAR(200) NULL DEFAULT NULL,
  `stock_type_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_api_UNIQUE` (`id_api` ASC),
  INDEX `fk_stock_stock_type_idx` (`stock_type_id` ASC),
  CONSTRAINT `fk_stock_stock_type`
    FOREIGN KEY (`stock_type_id`)
    REFERENCES `my_stocks_db`.`stock_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `my_stocks_db`.`finantial_movement`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `my_stocks_db`.`finantial_movement` ;

CREATE TABLE IF NOT EXISTS `my_stocks_db`.`finantial_movement` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `movement_date` DATE NOT NULL,
  `finantial_movement_type_id` INT(11) NOT NULL,
  `quantity` INT(11) NOT NULL,
  `unit_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `cost` DECIMAL(10,2) NULL DEFAULT '0.00',
  `stock_id` INT(11) NULL DEFAULT NULL,
  `broker_invoice_id` INT(11) NULL DEFAULT NULL,
  `broker_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_stock_transaction_stock1_idx` (`stock_id` ASC),
  INDEX `fk_stock_transaction_broker_invoice1_idx` (`broker_invoice_id` ASC),
  INDEX `fk_finantial_movement_finantial_movement_type1_idx` (`finantial_movement_type_id` ASC),
  INDEX `fk_finantial_movement_broker1_idx` (`broker_id` ASC),
  CONSTRAINT `fk_finantial_movement_broker1`
    FOREIGN KEY (`broker_id`)
    REFERENCES `my_stocks_db`.`broker` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_finantial_movement_finantial_movement_type1`
    FOREIGN KEY (`finantial_movement_type_id`)
    REFERENCES `my_stocks_db`.`finantial_movement_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_stock_transaction_broker_invoice1`
    FOREIGN KEY (`broker_invoice_id`)
    REFERENCES `my_stocks_db`.`broker_invoice` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_stock_transaction_stock1`
    FOREIGN KEY (`stock_id`)
    REFERENCES `my_stocks_db`.`stock` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `my_stocks_db`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `my_stocks_db`.`role` ;

CREATE TABLE IF NOT EXISTS `my_stocks_db`.`role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `my_stocks_db`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `my_stocks_db`.`user` ;

CREATE TABLE IF NOT EXISTS `my_stocks_db`.`user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(200) NULL DEFAULT NULL,
  `password` VARCHAR(500) NULL DEFAULT NULL,
  `account_non_expired` TINYINT(4) NULL DEFAULT '0',
  `account_non_locked` TINYINT(4) NULL DEFAULT '0',
  `credentials_non_expired` TINYINT(4) NULL DEFAULT '0',
  `enabled` TINYINT(4) NULL DEFAULT '0',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `my_stocks_db`.`user_roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `my_stocks_db`.`user_roles` ;

CREATE TABLE IF NOT EXISTS `my_stocks_db`.`user_roles` (
  `user_id` INT(11) NOT NULL,
  `role_id` INT(11) NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  INDEX `fk_user_has_role_role1_idx` (`role_id` ASC),
  INDEX `fk_user_has_role_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_user_has_role_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `my_stocks_db`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_role_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `my_stocks_db`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
