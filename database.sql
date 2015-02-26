


CREATE TABLE "album" (
"id"  SERIAL ,
"name_publisher" VARCHAR NOT NULL DEFAULT 'NULL' ,
"id_artist" INTEGER ,
"name" VARCHAR NOT NULL DEFAULT 'NULL' ,
"release_date" DATE ,
PRIMARY KEY ("id")
);

CREATE TABLE "customer" (
"id"  SERIAL ,
"firstname" VARCHAR ,
"lastname" VARCHAR ,
"mailadress" VARCHAR ,
PRIMARY KEY ("id")
);

CREATE TABLE "artist" (
"id"  SERIAL ,
"firstname" VARCHAR ,
"lastname" VARCHAR ,
PRIMARY KEY ("id")
);

CREATE TABLE "publisher" (
"name" VARCHAR NOT NULL DEFAULT 'NULL' ,
PRIMARY KEY ("name")
);

CREATE TABLE "track" (
"id"  SERIAL ,
"name" VARCHAR ,
"duration" INTEGER ,
"genre" VARCHAR ,
PRIMARY KEY ("id")
);

CREATE TABLE "reservation" (
"id"  SERIAL ,
"id_customer" INTEGER ,
"id_movie" INTEGER ,
"id_album" INTEGER ,
PRIMARY KEY ("id")
);

CREATE TABLE "album_tracks" (
"id_album" INTEGER ,
"id_tracks" INTEGER ,
"track_position" INTEGER 
);

CREATE TABLE "tracks_artists" (
"id_tracks" INTEGER ,
"id_artists" INTEGER 
);

CREATE TABLE "product" (
"id"  SERIAL ,
"id_album" INTEGER ,
"id_movie" INTEGER ,
"id_customer" INTEGER ,
"expected_return" DATE ,
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
"id_artist" INTEGER 
);

CREATE TABLE "rental_history" (
"id"  SERIAL ,
"id_product" INTEGER ,
"id_customer" INTEGER ,
"return_date" DATE ,
PRIMARY KEY ("id")
);

ALTER TABLE "album" ADD FOREIGN KEY ("name_publisher") REFERENCES "publisher" ("name");
ALTER TABLE "album" ADD FOREIGN KEY ("id_artist") REFERENCES "artist" ("id");
ALTER TABLE "reservation" ADD FOREIGN KEY ("id_customer") REFERENCES "customer" ("id");
ALTER TABLE "reservation" ADD FOREIGN KEY ("id_movie") REFERENCES "movie" ("id");
ALTER TABLE "reservation" ADD FOREIGN KEY ("id_album") REFERENCES "album" ("id");
ALTER TABLE "album_tracks" ADD FOREIGN KEY ("id_album") REFERENCES "album" ("id");
ALTER TABLE "album_tracks" ADD FOREIGN KEY ("id_tracks") REFERENCES "track" ("id");
ALTER TABLE "tracks_artists" ADD FOREIGN KEY ("id_tracks") REFERENCES "track" ("id");
ALTER TABLE "tracks_artists" ADD FOREIGN KEY ("id_artists") REFERENCES "artist" ("id");
ALTER TABLE "product" ADD FOREIGN KEY ("id_album") REFERENCES "album" ("id");
ALTER TABLE "product" ADD FOREIGN KEY ("id_movie") REFERENCES "movie" ("id");
ALTER TABLE "product" ADD FOREIGN KEY ("id_customer") REFERENCES "customer" ("id");
ALTER TABLE "movie" ADD FOREIGN KEY ("name_publisher") REFERENCES "publisher" ("name");
ALTER TABLE "movie_artists" ADD FOREIGN KEY ("id_movie") REFERENCES "movie" ("id");
ALTER TABLE "movie_artists" ADD FOREIGN KEY ("id_artist") REFERENCES "artist" ("id");
ALTER TABLE "rental_history" ADD FOREIGN KEY ("id_product") REFERENCES "product" ("id");
ALTER TABLE "rental_history" ADD FOREIGN KEY ("id_customer") REFERENCES "customer" ("id");

