


CREATE TABLE "album" (
"id"  SERIAL ,
"name_publisher" VARCHAR NOT NULL DEFAULT 'NULL' ,
"id_artists" INTEGER ,
"name" VARCHAR NOT NULL DEFAULT 'NULL' ,
"genre" VARCHAR ,
"release_date" DATE ,
PRIMARY KEY ("id")
);

CREATE TABLE "customers" (
"id"  SERIAL ,
"firstname" VARCHAR ,
"lastname" VARCHAR ,
"mailadress" VARCHAR ,
PRIMARY KEY ("id")
);

CREATE TABLE "artists" (
"id"  SERIAL ,
"firstname" VARCHAR ,
"lastname" VARCHAR ,
PRIMARY KEY ("id")
);

CREATE TABLE "publisher" (
"name" VARCHAR NOT NULL DEFAULT 'NULL' ,
PRIMARY KEY ("name")
);

CREATE TABLE "tracks" (
"id"  SERIAL ,
"name" VARCHAR ,
"duration" INTEGER ,
PRIMARY KEY ("id")
);

CREATE TABLE "reservation" (
"id"  SERIAL ,
"id_customers" INTEGER ,
"id_product" INTEGER ,
PRIMARY KEY ("id")
);

CREATE TABLE "album_tracks" (
"id_album" INTEGER ,
"id_tracks" INTEGER ,
"track_position" INTEGER ,
PRIMARY KEY ()
);

CREATE TABLE "album_artists" (
"id_album" INTEGER ,
"id_artists" INTEGER ,
PRIMARY KEY ()
);

CREATE TABLE "tracks_artists" (
"id_tracks" INTEGER ,
"id_artists" INTEGER ,
PRIMARY KEY ()
);

CREATE TABLE "product" (
"id"  SERIAL ,
"id_album" INTEGER ,
"id_movie" INTEGER ,
"stock" INTEGER ,
PRIMARY KEY ("id")
);

CREATE TABLE "movie" (
"id"  SERIAL ,
"name_publisher" VARCHAR NOT NULL DEFAULT 'NULL' ,
"name" VARCHAR ,
"duration" INTEGER ,
PRIMARY KEY ("id")
);

CREATE TABLE "movie_artists" (
"id_movie" INTEGER ,
"id_artists" INTEGER ,
PRIMARY KEY ()
);

CREATE TABLE "rentals" (
"id"  SERIAL ,
"id_product" INTEGER ,
"id_customers" INTEGER ,
"return_date" DATE ,
PRIMARY KEY ("id")
);

ALTER TABLE "album" ADD FOREIGN KEY ("name_publisher") REFERENCES "publisher" ("name");
ALTER TABLE "album" ADD FOREIGN KEY ("id_artists") REFERENCES "artists" ("id");
ALTER TABLE "reservation" ADD FOREIGN KEY ("id_customers") REFERENCES "customers" ("id");
ALTER TABLE "reservation" ADD FOREIGN KEY ("id_product") REFERENCES "product" ("id");
ALTER TABLE "album_tracks" ADD FOREIGN KEY ("id_album") REFERENCES "album" ("id");
ALTER TABLE "album_tracks" ADD FOREIGN KEY ("id_tracks") REFERENCES "tracks" ("id");
ALTER TABLE "album_artists" ADD FOREIGN KEY ("id_album") REFERENCES "album" ("id");
ALTER TABLE "album_artists" ADD FOREIGN KEY ("id_artists") REFERENCES "artists" ("id");
ALTER TABLE "tracks_artists" ADD FOREIGN KEY ("id_tracks") REFERENCES "tracks" ("id");
ALTER TABLE "tracks_artists" ADD FOREIGN KEY ("id_artists") REFERENCES "artists" ("id");
ALTER TABLE "product" ADD FOREIGN KEY ("id_album") REFERENCES "album" ("id");
ALTER TABLE "product" ADD FOREIGN KEY ("id_movie") REFERENCES "movie" ("id");
ALTER TABLE "movie" ADD FOREIGN KEY ("name_publisher") REFERENCES "publisher" ("name");
ALTER TABLE "movie_artists" ADD FOREIGN KEY ("id_movie") REFERENCES "movie" ("id");
ALTER TABLE "movie_artists" ADD FOREIGN KEY ("id_artists") REFERENCES "artists" ("id");
ALTER TABLE "rentals" ADD FOREIGN KEY ("id_product") REFERENCES "product" ("id");
ALTER TABLE "rentals" ADD FOREIGN KEY ("id_customers") REFERENCES "customers" ("id");

