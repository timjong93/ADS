--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.1
-- Dumped by pg_dump version 9.3.1
-- Started on 2015-03-01 19:30:23

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 194 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2089 (class 0 OID 0)
-- Dependencies: 194
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 207 (class 1255 OID 172495)
-- Name: Reservation(); Type: FUNCTION; Schema: public; Owner: ADT
--

CREATE FUNCTION "Reservation"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
	reservationCount integer;
	freeCount integer;
	nextCustomer integer;
BEGIN
	IF OLD.id_customer IS NULL AND NEW.id_customer IS NOT NULL THEN
		IF OLD.id_movie IS NULL THEN
			SELECT count(id) INTO freeCount FROM product where id_album = OLD.id_album AND id_customer IS NULL;
			SELECT count(id) INTO reservationCount FROM reservation where id_album = OLD.id_album;
			SELECT id_customer INTO nextCustomer FROM reservation where id_album = OLD.id_album ORDER BY id LIMIT 1;
		ELSE
			SELECT count(id) INTO freeCount FROM product where id_movie = OLD.id_movie AND id_customer IS NULL;
			SELECT count(id) INTO reservationCount FROM reservation where id_movie = OLD.id_movie;
			SELECT id_customer INTO nextCustomer FROM reservation where id_movie = OLD.id_movie ORDER BY id LIMIT 1;
		END IF;

		IF freeCount <= reservationCount THEN
			IF nextCustomer != NEW.id_customer THEN
				RAISE EXCEPTION 'Reserved for another customer';
			ELSE
				IF OLD.id_movie IS NULL THEN
					DELETE FROM reservation WHERE id_customer=NEW.id_customer AND id_album = OLD.id_album;
				ELSE		
					DELETE FROM reservation WHERE id_customer=NEW.id_customer AND id_movie = OLD.id_movie;
				END IF;
			END IF;
		END IF;
	END IF;

	RETURN NEW;
END$$;


ALTER FUNCTION public."Reservation"() OWNER TO "ADT";

--
-- TOC entry 208 (class 1255 OID 172496)
-- Name: add_album(integer, character varying, character varying, date, integer[]); Type: FUNCTION; Schema: public; Owner: ADT
--

CREATE FUNCTION add_album(artist_id integer, publisher character varying, name character varying, release_date date, track_ids integer[]) RETURNS integer
    LANGUAGE plpgsql
    AS $$DECLARE 
	result integer;
	i integer;
BEGIN
    INSERT INTO album(id_artist, name_publisher, name, release_date) VALUES(artist_id, publisher, name, release_date) RETURNING id INTO result;
    FOR i IN 1 .. array_upper(track_ids, 1) LOOP
        INSERT INTO album_tracks(id_album, id_track, track_position) values (result, track_ids[i], i);
    END LOOP;
    return result;
END;$$;


ALTER FUNCTION public.add_album(artist_id integer, publisher character varying, name character varying, release_date date, track_ids integer[]) OWNER TO "ADT";

--
-- TOC entry 209 (class 1255 OID 172497)
-- Name: add_movie(character varying, character varying, integer, integer[]); Type: FUNCTION; Schema: public; Owner: ADT
--

CREATE FUNCTION add_movie(publisher character varying, name character varying, duration integer, artistids integer[]) RETURNS integer
    LANGUAGE plpgsql
    AS $$DECLARE
	genid integer;
	i integer;
BEGIN
	INSERT INTO movie(name_publisher, name, duration) values (publisher, name, duration) RETURNING id INTO genid;
	FOR i IN 1 .. array_upper(artistids, 1) LOOP
		INSERT INTO movie_artists(id_movie, id_artist) values (genid, artistids[i]);
	END LOOP;
	return genid;
END;$$;


ALTER FUNCTION public.add_movie(publisher character varying, name character varying, duration integer, artistids integer[]) OWNER TO "ADT";

--
-- TOC entry 210 (class 1255 OID 172498)
-- Name: add_product(integer, integer, integer); Type: FUNCTION; Schema: public; Owner: ADT
--

CREATE FUNCTION add_product(movie_id integer, album_id integer, amount integer) RETURNS void
    LANGUAGE plpgsql
    AS $$DECLARE
	i integer;
BEGIN
	IF movie_id != -1 AND album_id != -1 THEN
		RAISE EXCEPTION 'a product cannot be both a movie and an album, set one param to -1'; 
	END IF;
	
	IF amount < 0 THEN
		RAISE EXCEPTION 'cannot create negative amount of copies'; 
	END IF;

	FOR i IN 1 .. amount LOOP
		INSERT INTO product(id_album,id_movie) VALUES (album_id, movie_id);
	END LOOP;
END;$$;


ALTER FUNCTION public.add_product(movie_id integer, album_id integer, amount integer) OWNER TO "ADT";

--
-- TOC entry 211 (class 1255 OID 172499)
-- Name: add_track(character varying, character varying, integer, integer[]); Type: FUNCTION; Schema: public; Owner: ADT
--

CREATE FUNCTION add_track(genre character varying, name character varying, duration integer, artistids integer[]) RETURNS integer
    LANGUAGE plpgsql
    AS $$DECLARE
 genid integer;
 i integer;
BEGIN
 INSERT INTO track(name, duration, genre) values (name, duration, genre) RETURNING id INTO genid;
 FOR i IN 1 .. array_upper(artistids, 1) LOOP
  INSERT INTO tracks_artists(id_track, id_artist) values (genid, artistids[i]);
 END LOOP;
 return genid;
END;$$;


ALTER FUNCTION public.add_track(genre character varying, name character varying, duration integer, artistids integer[]) OWNER TO "ADT";

--
-- TOC entry 212 (class 1255 OID 172500)
-- Name: is_movie_available(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION is_movie_available(movie_id integer) RETURNS bit
    LANGUAGE plpgsql
    AS $$DECLARE
	productcount integer;
BEGIN
	SELECT COUNT(id) FROM product where product.movie_id = movieid INTO productcount;
	return 1;
END$$;


ALTER FUNCTION public.is_movie_available(movie_id integer) OWNER TO postgres;

--
-- TOC entry 213 (class 1255 OID 172501)
-- Name: rent_album(integer, integer, date); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION rent_album(customer_id integer, album_id integer, return_date date) RETURNS integer
    LANGUAGE plpgsql
    AS $$DECLARE
	albumProduct integer;
BEGIN
	SELECT id INTO albumProduct FROM product WHERE product.id_album = album_id AND product.id_customer IS NULL;
	
	UPDATE product SET id_customer=customer_id,expected_return=return_date WHERE id = albumProduct;

	return albumProduct;
END;$$;


ALTER FUNCTION public.rent_album(customer_id integer, album_id integer, return_date date) OWNER TO postgres;

--
-- TOC entry 214 (class 1255 OID 172502)
-- Name: rent_movie(integer, integer, date); Type: FUNCTION; Schema: public; Owner: ADT
--

CREATE FUNCTION rent_movie(customer_id integer, movie_id integer, return_date date) RETURNS integer
    LANGUAGE plpgsql
    AS $$DECLARE
	movieProduct integer;
BEGIN
	SELECT id INTO movieProduct FROM product WHERE product.id_movie = movie_id AND product.id_customer IS NULL;
	
	UPDATE product SET id_customer=customer_id,expected_return=return_date WHERE id = movieProduct;

	return movieProduct;
END;$$;


ALTER FUNCTION public.rent_movie(customer_id integer, movie_id integer, return_date date) OWNER TO "ADT";

--
-- TOC entry 215 (class 1255 OID 172503)
-- Name: rental_history(); Type: FUNCTION; Schema: public; Owner: ADT
--

CREATE FUNCTION rental_history() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
	IF NEW.id_customer IS NULL AND OLD.id_customer IS NOT NULL THEN
		INSERT INTO rental_history(id_product, id_customer, return_date) values( OLD.id, OLD.id_customer, NOW());
	END IF;

	RETURN NEW;
END;$$;


ALTER FUNCTION public.rental_history() OWNER TO "ADT";

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 170 (class 1259 OID 172504)
-- Name: album; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE album (
    id integer NOT NULL,
    name_publisher character varying DEFAULT 'NULL'::character varying NOT NULL,
    id_artist integer,
    name character varying DEFAULT 'NULL'::character varying NOT NULL,
    release_date date
);


ALTER TABLE public.album OWNER TO postgres;

--
-- TOC entry 171 (class 1259 OID 172512)
-- Name: album_artists; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE album_artists (
    id_album integer,
    id_artists integer
);


ALTER TABLE public.album_artists OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 172515)
-- Name: album_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE album_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.album_id_seq OWNER TO postgres;

--
-- TOC entry 2090 (class 0 OID 0)
-- Dependencies: 172
-- Name: album_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE album_id_seq OWNED BY album.id;


--
-- TOC entry 173 (class 1259 OID 172517)
-- Name: album_tracks; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE album_tracks (
    id_album integer,
    id_track integer,
    track_position integer
);


ALTER TABLE public.album_tracks OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 172520)
-- Name: artist; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE artist (
    id integer NOT NULL,
    firstname character varying,
    lastname character varying
);


ALTER TABLE public.artist OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 172526)
-- Name: artists_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE artists_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.artists_id_seq OWNER TO postgres;

--
-- TOC entry 2091 (class 0 OID 0)
-- Dependencies: 175
-- Name: artists_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE artists_id_seq OWNED BY artist.id;


--
-- TOC entry 176 (class 1259 OID 172528)
-- Name: customer; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE customer (
    id integer NOT NULL,
    firstname character varying,
    lastname character varying,
    mailadress character varying
);


ALTER TABLE public.customer OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 172534)
-- Name: customers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE customers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.customers_id_seq OWNER TO postgres;

--
-- TOC entry 2092 (class 0 OID 0)
-- Dependencies: 177
-- Name: customers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE customers_id_seq OWNED BY customer.id;


--
-- TOC entry 178 (class 1259 OID 172536)
-- Name: movie; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE movie (
    id integer NOT NULL,
    name_publisher character varying DEFAULT 'NULL'::character varying NOT NULL,
    name character varying,
    duration integer
);


ALTER TABLE public.movie OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 172543)
-- Name: movie_artists; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE movie_artists (
    id_movie integer,
    id_artist integer
);


ALTER TABLE public.movie_artists OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 172546)
-- Name: movie_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE movie_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.movie_id_seq OWNER TO postgres;

--
-- TOC entry 2093 (class 0 OID 0)
-- Dependencies: 180
-- Name: movie_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE movie_id_seq OWNED BY movie.id;


--
-- TOC entry 181 (class 1259 OID 172548)
-- Name: movieproduct; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE movieproduct (
    id integer
);


ALTER TABLE public.movieproduct OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 172551)
-- Name: product; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE product (
    id integer NOT NULL,
    id_album integer,
    id_movie integer,
    id_customer integer,
    expected_return date
);


ALTER TABLE public.product OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 172554)
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.product_id_seq OWNER TO postgres;

--
-- TOC entry 2095 (class 0 OID 0)
-- Dependencies: 183
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE product_id_seq OWNED BY product.id;


--
-- TOC entry 184 (class 1259 OID 172556)
-- Name: publisher; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE publisher (
    name character varying DEFAULT 'NULL'::character varying NOT NULL
);


ALTER TABLE public.publisher OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 172563)
-- Name: rental_history; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rental_history (
    id integer NOT NULL,
    id_product integer,
    id_customer integer,
    return_date date
);


ALTER TABLE public.rental_history OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 172566)
-- Name: rental_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE rental_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rental_history_id_seq OWNER TO postgres;

--
-- TOC entry 2097 (class 0 OID 0)
-- Dependencies: 186
-- Name: rental_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE rental_history_id_seq OWNED BY rental_history.id;


--
-- TOC entry 187 (class 1259 OID 172568)
-- Name: rentals; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rentals (
    id integer NOT NULL,
    id_product integer,
    id_customers integer,
    return_date date
);


ALTER TABLE public.rentals OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 172571)
-- Name: rentals_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE rentals_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rentals_id_seq OWNER TO postgres;

--
-- TOC entry 2098 (class 0 OID 0)
-- Dependencies: 188
-- Name: rentals_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE rentals_id_seq OWNED BY rentals.id;


--
-- TOC entry 189 (class 1259 OID 172573)
-- Name: reservation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE reservation (
    id integer NOT NULL,
    id_customer integer,
    id_movie integer,
    id_album integer
);


ALTER TABLE public.reservation OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 172576)
-- Name: reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reservation_id_seq OWNER TO postgres;

--
-- TOC entry 2099 (class 0 OID 0)
-- Dependencies: 190
-- Name: reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE reservation_id_seq OWNED BY reservation.id;


--
-- TOC entry 191 (class 1259 OID 172578)
-- Name: track; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE track (
    id integer NOT NULL,
    name character varying,
    duration integer,
    genre character varying
);


ALTER TABLE public.track OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 172584)
-- Name: tracks_artists; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tracks_artists (
    id_track integer,
    id_artist integer
);


ALTER TABLE public.tracks_artists OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 172587)
-- Name: tracks_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tracks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tracks_id_seq OWNER TO postgres;

--
-- TOC entry 2100 (class 0 OID 0)
-- Dependencies: 193
-- Name: tracks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tracks_id_seq OWNED BY track.id;


--
-- TOC entry 1912 (class 2604 OID 172589)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY album ALTER COLUMN id SET DEFAULT nextval('album_id_seq'::regclass);


--
-- TOC entry 1913 (class 2604 OID 172590)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY artist ALTER COLUMN id SET DEFAULT nextval('artists_id_seq'::regclass);


--
-- TOC entry 1914 (class 2604 OID 172591)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY customer ALTER COLUMN id SET DEFAULT nextval('customers_id_seq'::regclass);


--
-- TOC entry 1916 (class 2604 OID 172592)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movie ALTER COLUMN id SET DEFAULT nextval('movie_id_seq'::regclass);


--
-- TOC entry 1917 (class 2604 OID 172593)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY product ALTER COLUMN id SET DEFAULT nextval('product_id_seq'::regclass);


--
-- TOC entry 1919 (class 2604 OID 172594)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rental_history ALTER COLUMN id SET DEFAULT nextval('rental_history_id_seq'::regclass);


--
-- TOC entry 1920 (class 2604 OID 172595)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rentals ALTER COLUMN id SET DEFAULT nextval('rentals_id_seq'::regclass);


--
-- TOC entry 1921 (class 2604 OID 172596)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY reservation ALTER COLUMN id SET DEFAULT nextval('reservation_id_seq'::regclass);


--
-- TOC entry 1922 (class 2604 OID 172597)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY track ALTER COLUMN id SET DEFAULT nextval('tracks_id_seq'::regclass);


--
-- TOC entry 1925 (class 2606 OID 172599)
-- Name: album_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY album
    ADD CONSTRAINT album_pkey PRIMARY KEY (id);


--
-- TOC entry 1931 (class 2606 OID 172601)
-- Name: artists_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY artist
    ADD CONSTRAINT artists_pkey PRIMARY KEY (id);


--
-- TOC entry 1933 (class 2606 OID 172603)
-- Name: customers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id);


--
-- TOC entry 1935 (class 2606 OID 172605)
-- Name: movie_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY movie
    ADD CONSTRAINT movie_pkey PRIMARY KEY (id);


--
-- TOC entry 1940 (class 2606 OID 172607)
-- Name: product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 1942 (class 2606 OID 172609)
-- Name: publisher_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY publisher
    ADD CONSTRAINT publisher_pkey PRIMARY KEY (name);


--
-- TOC entry 1946 (class 2606 OID 172611)
-- Name: rental_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rental_history
    ADD CONSTRAINT rental_history_pkey PRIMARY KEY (id);


--
-- TOC entry 1948 (class 2606 OID 172613)
-- Name: rentals_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rentals
    ADD CONSTRAINT rentals_pkey PRIMARY KEY (id);


--
-- TOC entry 1950 (class 2606 OID 172615)
-- Name: reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);


--
-- TOC entry 1953 (class 2606 OID 172617)
-- Name: tracks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY track
    ADD CONSTRAINT tracks_pkey PRIMARY KEY (id);


--
-- TOC entry 1923 (class 1259 OID 172725)
-- Name: album_name_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX album_name_idx ON album USING btree (name);


--
-- TOC entry 1926 (class 1259 OID 172719)
-- Name: album_tracks_id_album_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX album_tracks_id_album_idx ON album_tracks USING btree (id_album);


--
-- TOC entry 1927 (class 1259 OID 172720)
-- Name: album_tracks_id_track_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX album_tracks_id_track_idx ON album_tracks USING btree (id_track);


--
-- TOC entry 1928 (class 1259 OID 172721)
-- Name: artist_firstname_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX artist_firstname_idx ON artist USING btree (firstname);


--
-- TOC entry 1929 (class 1259 OID 172722)
-- Name: artist_lastname_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX artist_lastname_idx ON artist USING btree (lastname);


--
-- TOC entry 1938 (class 1259 OID 172618)
-- Name: fki_product_id_customer_fkey; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_product_id_customer_fkey ON product USING btree (id_customer);


--
-- TOC entry 1936 (class 1259 OID 172724)
-- Name: movie_artists_id_artist_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX movie_artists_id_artist_idx ON movie_artists USING btree (id_artist);


--
-- TOC entry 1937 (class 1259 OID 172723)
-- Name: movie_artists_id_movie_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX movie_artists_id_movie_idx ON movie_artists USING btree (id_movie);


--
-- TOC entry 1943 (class 1259 OID 172726)
-- Name: rental_history_id_customer_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX rental_history_id_customer_idx ON rental_history USING btree (id_customer);


--
-- TOC entry 1944 (class 1259 OID 172727)
-- Name: rental_history_id_product_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX rental_history_id_product_idx ON rental_history USING btree (id_product);


--
-- TOC entry 1951 (class 1259 OID 172717)
-- Name: track_name_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX track_name_idx ON track USING btree (name);


--
-- TOC entry 1973 (class 2620 OID 172619)
-- Name: History; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "History" BEFORE UPDATE ON product FOR EACH ROW EXECUTE PROCEDURE rental_history();


--
-- TOC entry 1974 (class 2620 OID 172620)
-- Name: reserve; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER reserve BEFORE UPDATE ON product FOR EACH ROW EXECUTE PROCEDURE "Reservation"();


--
-- TOC entry 1956 (class 2606 OID 172621)
-- Name: album_artists_id_album_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY album_artists
    ADD CONSTRAINT album_artists_id_album_fkey FOREIGN KEY (id_album) REFERENCES album(id);


--
-- TOC entry 1957 (class 2606 OID 172626)
-- Name: album_artists_id_artists_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY album_artists
    ADD CONSTRAINT album_artists_id_artists_fkey FOREIGN KEY (id_artists) REFERENCES artist(id);


--
-- TOC entry 1954 (class 2606 OID 172631)
-- Name: album_id_artists_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY album
    ADD CONSTRAINT album_id_artists_fkey FOREIGN KEY (id_artist) REFERENCES artist(id);


--
-- TOC entry 1955 (class 2606 OID 172636)
-- Name: album_name_publisher_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY album
    ADD CONSTRAINT album_name_publisher_fkey FOREIGN KEY (name_publisher) REFERENCES publisher(name);


--
-- TOC entry 1958 (class 2606 OID 172641)
-- Name: album_tracks_id_album_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY album_tracks
    ADD CONSTRAINT album_tracks_id_album_fkey FOREIGN KEY (id_album) REFERENCES album(id);


--
-- TOC entry 1959 (class 2606 OID 172646)
-- Name: album_tracks_id_tracks_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY album_tracks
    ADD CONSTRAINT album_tracks_id_tracks_fkey FOREIGN KEY (id_track) REFERENCES track(id);


--
-- TOC entry 1961 (class 2606 OID 172651)
-- Name: movie_artists_id_artists_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movie_artists
    ADD CONSTRAINT movie_artists_id_artists_fkey FOREIGN KEY (id_artist) REFERENCES artist(id);


--
-- TOC entry 1962 (class 2606 OID 172656)
-- Name: movie_artists_id_movie_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movie_artists
    ADD CONSTRAINT movie_artists_id_movie_fkey FOREIGN KEY (id_movie) REFERENCES movie(id);


--
-- TOC entry 1960 (class 2606 OID 172661)
-- Name: movie_name_publisher_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movie
    ADD CONSTRAINT movie_name_publisher_fkey FOREIGN KEY (name_publisher) REFERENCES publisher(name);


--
-- TOC entry 1963 (class 2606 OID 172666)
-- Name: product_id_album_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_id_album_fkey FOREIGN KEY (id_album) REFERENCES album(id);


--
-- TOC entry 1964 (class 2606 OID 172671)
-- Name: product_id_customer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_id_customer_fkey FOREIGN KEY (id_customer) REFERENCES customer(id);


--
-- TOC entry 1965 (class 2606 OID 172676)
-- Name: product_id_movie_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_id_movie_fkey FOREIGN KEY (id_movie) REFERENCES movie(id);


--
-- TOC entry 1966 (class 2606 OID 172681)
-- Name: rental_history_id_customer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rental_history
    ADD CONSTRAINT rental_history_id_customer_fkey FOREIGN KEY (id_customer) REFERENCES customer(id);


--
-- TOC entry 1967 (class 2606 OID 172686)
-- Name: rental_history_id_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rental_history
    ADD CONSTRAINT rental_history_id_product_fkey FOREIGN KEY (id_product) REFERENCES product(id);


--
-- TOC entry 1968 (class 2606 OID 172691)
-- Name: rentals_id_customers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rentals
    ADD CONSTRAINT rentals_id_customers_fkey FOREIGN KEY (id_customers) REFERENCES customer(id);


--
-- TOC entry 1969 (class 2606 OID 172696)
-- Name: rentals_id_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rentals
    ADD CONSTRAINT rentals_id_product_fkey FOREIGN KEY (id_product) REFERENCES product(id);


--
-- TOC entry 1970 (class 2606 OID 172701)
-- Name: reservation_id_customers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY reservation
    ADD CONSTRAINT reservation_id_customers_fkey FOREIGN KEY (id_customer) REFERENCES customer(id);


--
-- TOC entry 1971 (class 2606 OID 172706)
-- Name: tracks_artists_id_artists_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tracks_artists
    ADD CONSTRAINT tracks_artists_id_artists_fkey FOREIGN KEY (id_artist) REFERENCES artist(id);


--
-- TOC entry 1972 (class 2606 OID 172711)
-- Name: tracks_artists_id_tracks_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tracks_artists
    ADD CONSTRAINT tracks_artists_id_tracks_fkey FOREIGN KEY (id_track) REFERENCES track(id);


--
-- TOC entry 2088 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- TOC entry 2094 (class 0 OID 0)
-- Dependencies: 181
-- Name: movieproduct; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE movieproduct FROM PUBLIC;
REVOKE ALL ON TABLE movieproduct FROM postgres;
GRANT ALL ON TABLE movieproduct TO postgres;
GRANT ALL ON TABLE movieproduct TO PUBLIC;


--
-- TOC entry 2096 (class 0 OID 0)
-- Dependencies: 185
-- Name: rental_history; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE rental_history FROM PUBLIC;
REVOKE ALL ON TABLE rental_history FROM postgres;
GRANT ALL ON TABLE rental_history TO postgres;
GRANT ALL ON TABLE rental_history TO PUBLIC;


-- Completed on 2015-03-01 19:30:24

--
-- PostgreSQL database dump complete
--

