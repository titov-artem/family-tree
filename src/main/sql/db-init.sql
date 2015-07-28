CREATE DATABASE IF NOT EXISTS family_db
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

USE family_db;

CREATE TABLE IF NOT EXISTS person (
  id          BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name        VARCHAR(64)           NOT NULL,
  surname     VARCHAR(64)  DEFAULT NULL,
  description TINYTEXT     DEFAULT NULL,
  main_photo  VARCHAR(128) DEFAULT NULL
)
  ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS family_relations (
  family_id BIGINT NOT NULL,
  parent_id BIGINT NOT NULL,
  child_id  BIGINT NOT NULL,
  PRIMARY KEY (family_id, parent_id, child_id),
  INDEX (parent_id),
  INDEX (child_id)
)
  ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS family (
  family_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
)
  ENGINE = INNODB;