-- SQLite database
ALTER TABLE `<prefix>players` ADD COLUMN 'whitelisted' INTEGER DEFAULT 0 NOT NULL;