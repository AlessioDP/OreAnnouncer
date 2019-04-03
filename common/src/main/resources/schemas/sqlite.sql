-- Start and end placeholders are used to obtain right queries

/*START_PLAYERS*/
CREATE TABLE '{table_players}' (
	'uuid'			VARCHAR({varcharsize}) NOT NULL PRIMARY KEY,
	'name'			VARCHAR({varcharsize}) DEFAULT '',
	'alerts'		INTEGER DEFAULT 1 NOT NULL);
/*END_PLAYERS*/

/*START_BLOCKS*/
CREATE TABLE '{table_blocks}' (
	'player'			VARCHAR({varcharsize}) NOT NULL,
	'material_name'		VARCHAR({varcharsize}) NOT NULL,
	'destroyed'			INTEGER DEFAULT 0,
	PRIMARY KEY ('player', 'material_name'));
/*END_BLOCKS*/

/*START_VERSIONS*/
CREATE TABLE '{table_versions}' (
	'name'		VARCHAR({varcharsize}) NOT NULL PRIMARY KEY,
	'version'	INTEGER NOT NULL);
/*END_VERSIONS*/