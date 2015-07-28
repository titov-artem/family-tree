CREATE DATABASE IF NOT EXISTS family_db
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

USE family_db;

CREATE TABLE IF NOT EXISTS person (
  id          BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
  name        VARCHAR(64)           NOT NULL,
  surname     VARCHAR(64)  DEFAULT NULL,
  description TEXT         DEFAULT NULL,
  main_photo  VARCHAR(128) DEFAULT NULL
)
  ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS family_child (
  family_id BIGINT NOT NULL,
  parent_id BIGINT NOT NULL,
  child_id  BIGINT NOT NULL,
  PRIMARY KEY (family_id, parent_id, child_id),
  INDEX (family_id),
  INDEX (parent_id),
  INDEX (child_id)
)
  ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS family_spouses (
  family_id BIGINT NOT NULL,
  spouse1   BIGINT NOT NULL,
  spouse2   BIGINT NOT NULL,
  PRIMARY KEY (family_id, spouse1, spouse2),
  INDEX (family_id)
)
  ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS family (
  id       BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  owner_id BIGINT NOT NULL,
  INDEX (owner_id)
)
  ENGINE = INNODB;