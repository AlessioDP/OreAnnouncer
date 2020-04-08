-- SQLite database
CREATE TABLE IF NOT EXISTS `${prefix}players` (
	'uuid'			VARCHAR NOT NULL PRIMARY KEY,
	'alerts'		INTEGER DEFAULT 1 NOT NULL
);

CREATE TABLE IF NOT EXISTS `${prefix}blocks` (
	'player'			VARCHAR NOT NULL,
	'material_name'		VARCHAR NOT NULL,
	'destroyed'			INTEGER DEFAULT 0,
	PRIMARY KEY ('player', 'material_name')
);

CREATE TABLE IF NOT EXISTS `${prefix}blocks_found` (
	'id'				INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	'player'			VARCHAR NOT NULL,
	'material_name'		VARCHAR NOT NULL,
	'timestamp'			INTEGER NOT NULL,
	'found'				INTEGER DEFAULT 0
);