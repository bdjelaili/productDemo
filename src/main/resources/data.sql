CREATE TABLE phone (
  id                INT AUTO_INCREMENT  PRIMARY KEY,
  price             float8              NOT NULL,
  address           VARCHAR(250)        NOT NULL,
  city              VARCHAR(250)        NOT NULL,
  color             VARCHAR(250)        NOT NULL
);

CREATE TABLE subscription (
  id                INT AUTO_INCREMENT  PRIMARY KEY,
  price             float8              NOT NULL,
  address           VARCHAR(250)        NOT NULL,
  city              VARCHAR(250)        NOT NULL,
  gbLimit           int8                NOT NULL
);