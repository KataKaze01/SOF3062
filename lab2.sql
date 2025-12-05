create database J6Security2
use J6Security2

CREATE TABLE J6Users
(
    Username VARCHAR(50) NOT NULL,
    Password VARCHAR(500) NOT NULL,
    Enabled BIT NOT NULL,
    PRIMARY KEY(Username)
);

INSERT INTO J6Users VALUES('user@gmail.com', '{noop}123', 1);
INSERT INTO J6Users VALUES('admin@gmail.com', '{noop}123', 1);
INSERT INTO J6Users VALUES('both@gmail.com', '{noop}123', 1);

CREATE TABLE J6Roles
(
    Id VARCHAR(50) NOT NULL,
    Name VARCHAR(50) NOT NULL,
    PRIMARY KEY(Id)
);

INSERT INTO J6Roles VALUES('ROLE_USER', 'Users');
INSERT INTO J6Roles VALUES('ROLE_ADMIN', 'Administrators');

CREATE TABLE J6UserRoles
(
    Id BIGINT NOT NULL identity(1, 1),
    Username VARCHAR(50) NOT NULL,
    RoleId VARCHAR(50) NOT NULL,
    PRIMARY KEY(Id),
    UNIQUE(Username, RoleId),
    FOREIGN KEY(Username) REFERENCES J6Users(Username)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(RoleId) REFERENCES J6Roles(Id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO J6UserRoles(Username, RoleId) VALUES('user@gmail.com', 'ROLE_USER');
INSERT INTO J6UserRoles(Username, RoleId) VALUES('admin@gmail.com', 'ROLE_ADMIN');
INSERT INTO J6UserRoles(Username, RoleId) VALUES('both@gmail.com', 'ROLE_USER');
INSERT INTO J6UserRoles(Username, RoleId) VALUES('both@gmail.com', 'ROLE_ADMIN');

CREATE TABLE Users
(
Username VARCHAR(50) NOT NULL,
Password VARCHAR(500) NOT NULL,
Enabled BIT NOT NULL,
PRIMARY KEY(Username)
)
CREATE TABLE Authorities
(
Id BIGINT NOT NULL identity(1, 1),
Username VARCHAR(50) NOT NULL,
Authority VARCHAR(50) NOT NULL,
PRIMARY KEY(Id),
UNIQUE(Username, Authority),
FOREIGN KEY(Username) REFERENCES Users(Username)
ON DELETE CASCADE ON UPDATE CASCADE
)
INSERT INTO Users(Username, Password, Enabled) VALUES('user@gmail.com', '{noop}123', 1);
INSERT INTO Users(Username, Password, Enabled) VALUES('admin@gmail.com', '{noop}123', 1);
INSERT INTO Users(Username, Password, Enabled) VALUES('both@gmail.com', '{noop}123', 1);

INSERT INTO Authorities(Username, Authority) VALUES('user@gmail.com', 'ROLE_USER');
INSERT INTO Authorities(Username, Authority) VALUES('admin@gmail.com', 'ROLE_ADMIN'); 
INSERT INTO Authorities(Username, Authority) VALUES('both@gmail.com', 'ROLE_USER');
INSERT INTO Authorities(Username, Authority) VALUES('both@gmail.com', 'ROLE_ADMIN');