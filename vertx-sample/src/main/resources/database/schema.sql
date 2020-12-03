
CREATE DATABASE eirdb

CREATE TABLE imei (
imei varchar(16),
status int,
PRIMARY KEY (imei));

CREATE TABLE imsi (
imsi varchar(15),
status int,
PRIMARY KEY (imsi));

CREATE TABLE imsi_msisdn (
imsi varchar(15),
msisdn varchar(15),
status int,
PRIMARY KEY (imsi, msisdn));

INSERT INTO imei (imei, status) values ('123456789012345', 1);
INSERT INTO imsi (imsi, status) values ('123456789012345', 1);
INSERT INTO imsi_msisdn (imsi, msisdn, status) values ('123456789012343','225668841', 1);