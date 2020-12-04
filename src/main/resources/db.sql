CREATE DATABASE emt_db;

USE emt_db;

CREATE TABLE emt_users (
	Id INT(3) NOT NULL AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL,
	hash VARCHAR(100) NOT NULL,
	mail VARCHAR(100) NOT NULL,
	PRIMARY KEY (Id)
);

CREATE TABLE emt_cardata (
	Id INT(5) NOT NULL AUTO_INCREMENT,
	vitesse VARCHAR(20) NOT NULL,
	consommation VARCHAR(20) NOT NULL,
	longitude VARCHAR(20) NOT NULL,
	latitude VARCHAR(20) NOT NULL,
	altitude VARCHAR(20) NOT NULL,
	tension VARCHAR(20) NOT NULL,
	intensite VARCHAR(20) NOT NULL,
	NbTours VARCHAR(20) NOT NULL,
	temps VARCHAR(20) NOT NULL,
	date VARCHAR(20) NOT NULL,
	PRIMARY KEY (Id)
);

CREATE TABLE emt_users_access (
	Id INT(5) NOT NULL,
	access VARCHAR(20) NOT NULL
);

CREATE TABLE emt_map_signal (
	Id VARCHAR(20) NOT NULL,
	lng VARCHAR(20) NOT NULL,
	lat VARCHAR(20) NOT NULL,
	type VARCHAR(20) NOT NULL,
	PRIMARY KEY (Id)
);

CREATE TABLE emt_dashboard (
	running VARCHAR(20) NOT NULL,
	PRIMARY KEY (running)
);

insert into emt_dashboard values 'false';

CREATE TABLE emt_map (
	Id INT(5) NOT NULL AUTO_INCREMENT,
        points VARCHAR(1000) NOT NULL,
	PRIMARY KEY (Id)
);

insert into emt_map(points) values ('[]');

CREATE TABLE emt_keys (
	access VARCHAR(20) NOT NULL,
	publicKey VARCHAR(2000) NOT NULL,
	privateKey VARCHAR(2000) NOT NULL,
	PRIMARY KEY (access)
);

CREATE TABLE emt_speed_control (
	speedUp VARCHAR(20) NOT NULL,
	slowDown VARCHAR(20) NOT NULL
);

insert into emt_speed_control(speedUp, slowDown) values ('false', 'false');

CREATE TABLE emt_user_recovery (
	name VARCHAR(20) NOT NULL,
	code VARCHAR(4) NOT NULL
);