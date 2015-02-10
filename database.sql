



-- ---
-- Globals
-- ---

-- SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
-- SET FOREIGN_KEY_CHECKS=0;

-- ---
-- Table 'album'
-- 
-- ---

DROP TABLE IF EXISTS 'album';
		
CREATE TABLE 'album' (
  'id' INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  'name_publisher' VARCHAR NOT NULL DEFAULT 'NULL',
  'id_artists' INTEGER NULL DEFAULT NULL,
  'name' VARCHAR NOT NULL DEFAULT 'NULL',
  'genre' VARCHAR NULL DEFAULT NULL,
  'release_date' DATE NULL DEFAULT NULL,
  PRIMARY KEY ('id')
);

-- ---
-- Table 'customers'
-- 
-- ---

DROP TABLE IF EXISTS 'customers';
		
CREATE TABLE 'customers' (
  'id' INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  'firstname' VARCHAR NULL DEFAULT NULL,
  'lastname' VARCHAR NULL DEFAULT NULL,
  'mailadress' VARCHAR NULL DEFAULT NULL,
  PRIMARY KEY ('id')
);

-- ---
-- Table 'artists'
-- 
-- ---

DROP TABLE IF EXISTS 'artists';
		
CREATE TABLE 'artists' (
  'id' INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  'firstname' VARCHAR NULL DEFAULT NULL,
  'lastname' VARCHAR NULL DEFAULT NULL,
  PRIMARY KEY ('id')
);

-- ---
-- Table 'publisher'
-- 
-- ---

DROP TABLE IF EXISTS 'publisher';
		
CREATE TABLE 'publisher' (
  'name' VARCHAR NOT NULL DEFAULT 'NULL',
  PRIMARY KEY ('name')
);

-- ---
-- Table 'tracks'
-- 
-- ---

DROP TABLE IF EXISTS 'tracks';
		
CREATE TABLE 'tracks' (
  'id' INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  'name' VARCHAR NULL DEFAULT NULL,
  'duration' INTEGER NULL DEFAULT NULL,
  PRIMARY KEY ('id')
);

-- ---
-- Table 'reservation'
-- 
-- ---

DROP TABLE IF EXISTS 'reservation';
		
CREATE TABLE 'reservation' (
  'id' INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  'id_customers' INTEGER NULL DEFAULT NULL,
  'id_product' INTEGER NULL DEFAULT NULL,
  PRIMARY KEY ('id')
);

-- ---
-- Table 'album_tracks'
-- 
-- ---

DROP TABLE IF EXISTS 'album_tracks';
		
CREATE TABLE 'album_tracks' (
  'id_album' INTEGER NULL DEFAULT NULL,
  'id_tracks' INTEGER NULL DEFAULT NULL,
  'track_position' INTEGER NULL DEFAULT NULL,
  PRIMARY KEY ()
);

-- ---
-- Table 'album_artists'
-- 
-- ---

DROP TABLE IF EXISTS 'album_artists';
		
CREATE TABLE 'album_artists' (
  'id_album' INTEGER NULL DEFAULT NULL,
  'id_artists' INTEGER NULL DEFAULT NULL,
  PRIMARY KEY ()
);

-- ---
-- Table 'tracks_artists'
-- 
-- ---

DROP TABLE IF EXISTS 'tracks_artists';
		
CREATE TABLE 'tracks_artists' (
  'id_tracks' INTEGER NULL DEFAULT NULL,
  'id_artists' INTEGER NULL DEFAULT NULL,
  PRIMARY KEY ()
);

-- ---
-- Table 'product'
-- 
-- ---

DROP TABLE IF EXISTS 'product';
		
CREATE TABLE 'product' (
  'id' INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  'id_album' INTEGER NULL DEFAULT NULL,
  'id_movie' INTEGER NULL DEFAULT NULL,
  'stock' INTEGER NULL DEFAULT NULL,
  PRIMARY KEY ('id')
);

-- ---
-- Table 'movie'
-- 
-- ---

DROP TABLE IF EXISTS 'movie';
		
CREATE TABLE 'movie' (
  'id' INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  'name_publisher' VARCHAR NOT NULL DEFAULT 'NULL',
  'name' VARCHAR NULL DEFAULT NULL,
  'duration' INTEGER NULL DEFAULT NULL,
  PRIMARY KEY ('id')
);

-- ---
-- Table 'movie_artists'
-- 
-- ---

DROP TABLE IF EXISTS 'movie_artists';
		
CREATE TABLE 'movie_artists' (
  'id_movie' INTEGER NULL DEFAULT NULL,
  'id_artists' INTEGER NULL DEFAULT NULL,
  PRIMARY KEY ()
);

-- ---
-- Table 'rentals'
-- 
-- ---

DROP TABLE IF EXISTS 'rentals';
		
CREATE TABLE 'rentals' (
  'id' INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  'id_product' INTEGER NULL DEFAULT NULL,
  'id_customers' INTEGER NULL DEFAULT NULL,
  'return_date' DATE NULL DEFAULT NULL,
  PRIMARY KEY ('id')
);

-- ---
-- Foreign Keys 
-- ---

ALTER TABLE 'album' ADD FOREIGN KEY (name_publisher) REFERENCES 'publisher' ('name');
ALTER TABLE 'album' ADD FOREIGN KEY (id_artists) REFERENCES 'artists' ('id');
ALTER TABLE 'reservation' ADD FOREIGN KEY (id_customers) REFERENCES 'customers' ('id');
ALTER TABLE 'reservation' ADD FOREIGN KEY (id_product) REFERENCES 'product' ('id');
ALTER TABLE 'album_tracks' ADD FOREIGN KEY (id_album) REFERENCES 'album' ('id');
ALTER TABLE 'album_tracks' ADD FOREIGN KEY (id_tracks) REFERENCES 'tracks' ('id');
ALTER TABLE 'album_artists' ADD FOREIGN KEY (id_album) REFERENCES 'album' ('id');
ALTER TABLE 'album_artists' ADD FOREIGN KEY (id_artists) REFERENCES 'artists' ('id');
ALTER TABLE 'tracks_artists' ADD FOREIGN KEY (id_tracks) REFERENCES 'tracks' ('id');
ALTER TABLE 'tracks_artists' ADD FOREIGN KEY (id_artists) REFERENCES 'artists' ('id');
ALTER TABLE 'product' ADD FOREIGN KEY (id_album) REFERENCES 'album' ('id');
ALTER TABLE 'product' ADD FOREIGN KEY (id_movie) REFERENCES 'movie' ('id');
ALTER TABLE 'movie' ADD FOREIGN KEY (name_publisher) REFERENCES 'publisher' ('name');
ALTER TABLE 'movie_artists' ADD FOREIGN KEY (id_movie) REFERENCES 'movie' ('id');
ALTER TABLE 'movie_artists' ADD FOREIGN KEY (id_artists) REFERENCES 'artists' ('id');
ALTER TABLE 'rentals' ADD FOREIGN KEY (id_product) REFERENCES 'product' ('id');
ALTER TABLE 'rentals' ADD FOREIGN KEY (id_customers) REFERENCES 'customers' ('id');

-- ---
-- Table Properties
-- ---

-- ALTER TABLE 'album' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'customers' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'artists' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'publisher' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'tracks' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'reservation' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'album_tracks' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'album_artists' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'tracks_artists' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'product' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'movie' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'movie_artists' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE 'rentals' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ---
-- Test Data
-- ---

-- INSERT INTO 'album' ('id','name_publisher','id_artists','name','genre','release_date') VALUES
-- ('','','','','','');
-- INSERT INTO 'customers' ('id','firstname','lastname','mailadress') VALUES
-- ('','','','');
-- INSERT INTO 'artists' ('id','firstname','lastname') VALUES
-- ('','','');
-- INSERT INTO 'publisher' ('name') VALUES
-- ('');
-- INSERT INTO 'tracks' ('id','name','duration') VALUES
-- ('','','');
-- INSERT INTO 'reservation' ('id','id_customers','id_product') VALUES
-- ('','','');
-- INSERT INTO 'album_tracks' ('id_album','id_tracks','track_position') VALUES
-- ('','','');
-- INSERT INTO 'album_artists' ('id_album','id_artists') VALUES
-- ('','');
-- INSERT INTO 'tracks_artists' ('id_tracks','id_artists') VALUES
-- ('','');
-- INSERT INTO 'product' ('id','id_album','id_movie','stock') VALUES
-- ('','','','');
-- INSERT INTO 'movie' ('id','name_publisher','name','duration') VALUES
-- ('','','','');
-- INSERT INTO 'movie_artists' ('id_movie','id_artists') VALUES
-- ('','');
-- INSERT INTO 'rentals' ('id','id_product','id_customers','return_date') VALUES
-- ('','','','');

