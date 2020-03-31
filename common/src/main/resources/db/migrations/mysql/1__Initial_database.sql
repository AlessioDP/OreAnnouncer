-- MySQL database
CREATE TABLE IF NOT EXISTS `${prefix}players` (
	`uuid`			VARCHAR(255) NOT NULL PRIMARY KEY,
	`alerts`		INTEGER DEFAULT 1 NOT NULL
);

CREATE TABLE IF NOT EXISTS `${prefix}blocks` (
	`player`			VARCHAR(255) NOT NULL,
	`material_name`		VARCHAR(255) NOT NULL,
	`destroyed`			INTEGER DEFAULT 0,
	PRIMARY KEY (`player`, `material_name`)
);

CREATE TABLE IF NOT EXISTS `${prefix}blocks_found` (
	`id`				INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`player`			VARCHAR(255) NOT NULL,
	`material_name`		VARCHAR(255) NOT NULL,
	`timestamp`			INTEGER NOT NULL,
	`found`				INTEGER DEFAULT 0
);