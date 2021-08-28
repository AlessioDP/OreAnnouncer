-- PostgreSQL database
CREATE TABLE IF NOT EXISTS <prefix>players (
	"uuid"			CHAR(36) NOT NULL PRIMARY KEY,
	"alerts"		BOOL DEFAULT TRUE NOT NULL,
	"whitelisted"	BOOL DEFAULT FALSE NOT NULL
);

CREATE TABLE IF NOT EXISTS <prefix>blocks (
	"player"			CHAR(36) NOT NULL,
	"material_name"		VARCHAR(100) NOT NULL,
	"destroyed"			INTEGER DEFAULT 0,
	PRIMARY KEY ("player", "material_name")
);

CREATE TABLE IF NOT EXISTS <prefix>blocks_found (
	"id"				SERIAL PRIMARY KEY,
	"player"			CHAR(36) NOT NULL,
	"material_name"		VARCHAR(100) NOT NULL,
	"timestamp"			INTEGER NOT NULL,
	"found"				INTEGER DEFAULT 0
);