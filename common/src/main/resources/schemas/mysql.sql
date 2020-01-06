-- Start and end placeholders are used to obtain right queries

/*START_PLAYERS*/
CREATE TABLE `{table_players}` (
	`uuid`			VARCHAR({varcharsize}) NOT NULL,
	`alerts`		TINYINT(1) DEFAULT 1 NOT NULL,
	PRIMARY KEY (`uuid`))
 DEFAULT CHARSET='{charset}';
/*END_PLAYERS*/

/*START_BLOCKS*/
CREATE TABLE `{table_blocks}` (
	`player`			VARCHAR({varcharsize}) NOT NULL,
	`material_name`		VARCHAR({varcharsize}) NOT NULL,
	`destroyed`			INT DEFAULT 0,
	PRIMARY KEY (`player`, `material_name`))
 DEFAULT CHARSET='{charset}';
/*END_BLOCKS*/

/*START_BLOCKS_FOUND*/
CREATE TABLE `{table_blocks_found}` (
	`id`				INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`player`			VARCHAR({varcharsize}) NOT NULL,
	`material_name`		VARCHAR({varcharsize}) NOT NULL,
	`timestamp`			BIGINT NOT NULL,
	`found`				INT DEFAULT 0);
/*END_BLOCKS_FOUND*/

/*START_VERSIONS*/
CREATE TABLE `{table_versions}` (
	`name`		VARCHAR({varcharsize}) NOT NULL,
	`version`	INT NOT NULL,
	PRIMARY KEY (`name`))
 DEFAULT CHARSET='{charset}';
/*END_VERSIONS*/