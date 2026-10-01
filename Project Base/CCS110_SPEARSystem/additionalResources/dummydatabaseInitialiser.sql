/*
!NOTICE! - Run this SQL File to initialise a dummy database and create user dependencies required to run/test the S.P.E.A.R SYSTEM.
*/
CREATE DATABASE IF NOT EXISTS spearDatabase;
USE spearDatabase;

CREATE TABLE userAccounts(
username varchar(100),
pass varchar(100)


);

insert into userAccounts values ('admin', password('admin')), ('user', password('userPass'));

select * from userAccounts;

CREATE TABLE custodians(
    custodianID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    position VARCHAR(100),
    department VARCHAR(100),
    contactNumber VARCHAR(20),
    roomID INT
);

CREATE TABLE equipment_items(
    equipmentID INT AUTO_INCREMENT PRIMARY KEY,
    propertyTag VARCHAR(30) NOT NULL UNIQUE,
    equipmentName VARCHAR(150) NOT NULL,
    categoryID INT NOT NULL,
    brandModel VARCHAR(150),
    serialNumber VARCHAR(100),
    description VARCHAR(225) NOT NULL,
    roomID INT NOT NULL,
    custodianID INT,
    purchaseDate DATE,
    purchaseCost DECIMAL(12, 2) DEFAULT 0.00,
    condition_status VARCHAR(50) DEFAULT 'Good',
    availability VARCHAR(50) DEFAULT 'Available'
);

create user 'authenticator'@'localhost' identified by 'userAuthProfile';
grant select on userAccounts to 'authenticator'@'localhost';
flush privileges;
create user 'custodian'@'localhost' identified by 'custodianAccess';
grant select, insert, delete on equipment_items to 'custodian'@'localhost';
flush privileges;