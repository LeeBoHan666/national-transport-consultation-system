CREATE DATABASE IF NOT EXISTS city
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;
USE city;

CREATE TABLE IF NOT EXISTS city (
    CityID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    longitude DOUBLE NOT NULL,
    latitude DOUBLE NOT NULL,
    RouteID TEXT NULL COMMENT 'Comma-separated RouteID values used by the current application'
);

CREATE TABLE IF NOT EXISTS route (
    RouteID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `from` VARCHAR(100) NOT NULL,
    `to` VARCHAR(100) NOT NULL,
    way INT NOT NULL COMMENT 'Transport mode code used by the application',
    number VARCHAR(40) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    `time` VARCHAR(40) NOT NULL COMMENT 'Duration format consumed by the route calculator',
    departure VARCHAR(40) NOT NULL,
    arrival VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS `user` (
    ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Role VARCHAR(20) NOT NULL DEFAULT 'user'
);
