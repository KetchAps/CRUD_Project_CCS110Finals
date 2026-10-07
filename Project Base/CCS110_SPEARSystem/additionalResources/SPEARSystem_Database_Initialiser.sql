drop database if exists spearDatabase;
CREATE DATABASE IF NOT EXISTS spearDatabase;
USE spearDatabase;
-- ---------------------------------------------------------------------
-- 1. ROOMS / OFFICES
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS rooms_offices(
    roomID INT AUTO_INCREMENT PRIMARY KEY,
    roomName VARCHAR(100) NOT NULL,
    building VARCHAR(100) NOT NULL,
    floorNumber VARCHAR(20),
    roomType VARCHAR(50) DEFAULT 'Other',
    UNIQUE KEY uqRoom (roomName, building)
);

-- ---------------------------------------------------------------------
-- 2. CUSTODIANS / STAFF
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS custodians(
    custodianID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    position VARCHAR(100),
    department VARCHAR(100),
    contactNumber VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS userAccounts(
userID int,
username varchar(100) unique,
password varchar(255),
isAdmin bool,
foreign key (userID) references custodians(custodianID)
ON DELETE SET NULL ON UPDATE CASCADE
);

select * from userAccounts;
-- ---------------------------------------------------------------------
-- 3. CATEGORIES
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS equipment_categories(
    categoryName VARCHAR(100) PRIMARY KEY,
    description VARCHAR(255)
);

-- ---------------------------------------------------------------------
-- 4. INVENTORY (renamed from inventory_items)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS inventory(
    equipmentID INT AUTO_INCREMENT PRIMARY KEY,
    propertyTag VARCHAR(30) NOT NULL UNIQUE,
    equipmentName VARCHAR(150) NOT NULL,
    categoryName VARCHAR(100) NOT NULL,
    brandModel VARCHAR(150),
    serialNumber VARCHAR(100),
    description VARCHAR(255) NOT NULL,
    roomID INT,
    purchaseDate DATE,
    purchaseCost DECIMAL(12, 2),
    condition_status VARCHAR(50) DEFAULT 'Good',
    availability VARCHAR(50) DEFAULT 'Available',
    date_added DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoryName) REFERENCES equipment_categories(categoryName)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (roomID) REFERENCES rooms_offices(roomID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idxAvailability (availability)
);

-- ---------------------------------------------------------------------
-- 5. BORROWERS
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS borrowers(
    borrowerID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    borrowerType VARCHAR(50) NOT NULL,
    idNumber VARCHAR(30) NOT NULL UNIQUE,
    department_or_grade VARCHAR(100),
    contactNumber VARCHAR(20),
    email VARCHAR(100) UNIQUE
);

-- ---------------------------------------------------------------------
-- 6. BORROW / RETURN TRANSACTIONS (borrow_details merged in)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS borrow_transaction(
    transactionID INT AUTO_INCREMENT PRIMARY KEY,
    borrowerID INT NOT NULL,
    equipmentID INT NOT NULL,
    borrowDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expectedReturnDate DATE,
    actualReturnDate DATE,
    receivedBy INT,
    itemConditionUponBorrow VARCHAR(50) DEFAULT 'Good',
    itemConditionUponReturn VARCHAR(50),   -- NULL = not yet returned
    status VARCHAR(50) DEFAULT 'Borrowed',
    remarks VARCHAR(255),
    FOREIGN KEY (borrowerID) REFERENCES borrowers(borrowerID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (equipmentID) REFERENCES inventory(equipmentID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (receivedBy) REFERENCES custodians(custodianID)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CHECK (actualReturnDate IS NULL OR actualReturnDate >= borrowDate),
    INDEX idxStatus (status),
    INDEX idxExpectedReturn (expectedReturnDate)
);

-- ---------------------------------------------------------------------
-- 7. MAINTENANCE RECORDS
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS maintenance_records(
    maintenanceID INT AUTO_INCREMENT PRIMARY KEY,
    equipmentID INT NOT NULL,
    reportedDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reportedBy INT,
    issueDescription TEXT NOT NULL,
    actionTaken TEXT,
    cost DECIMAL(10, 2),
    maintenanceStatus VARCHAR(50) DEFAULT 'In Progress',
    FOREIGN KEY (equipmentID) REFERENCES inventory(equipmentID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (reportedBy) REFERENCES custodians(custodianID)
        ON DELETE SET NULL ON UPDATE CASCADE
);

select * from maintenance_records;
-- ---------------------------------------------------------------------
-- 8. USER INITIALISATION
-- ---------------------------------------------------------------------
create user if not exists 'authenticator'@'localhost' identified by 'userAuthProfile';
grant select, update, insert, delete on userAccounts to 'authenticator'@'localhost';
grant select, update, insert, delete on inventory to 'authenticator'@'localhost';
grant select, update, insert, delete on custodians to 'authenticator'@'localhost';
grant select, update, insert, delete on borrowers to 'authenticator'@'localhost';
grant select, update, insert, delete on borrow_transaction to 'authenticator'@'localhost';
grant select, update, insert, delete on maintenance_records to 'authenticator'@'localhost';

flush privileges;

-- ---------------------------------------------------------------------
-- 9. VALUE INITIALISATION
-- ---------------------------------------------------------------------


insert into equipment_categories values ('Furniture', ''), ('Electronics', '');

insert into rooms_offices values
(roomID, 'BCH 101', 'Bagong Cabuyao Hall', '1', 'Classroom'),
(roomID, 'BCH 102', 'Bagong Cabuyao Hall', '1', 'Classroom'),
(roomID, 'BCH 103', 'Bagong Cabuyao Hall', '1', 'Classroom'),
(roomID, 'BCH 201', 'Bagong Cabuyao Hall', '2', 'Classroom'),
(roomID, 'BCH 202', 'Bagong Cabuyao Hall', '2', 'Classroom'),
(roomID, 'BCH 203', 'Bagong Cabuyao Hall', '2', 'Classroom'),
(roomID, 'BCH 301', 'Bagong Cabuyao Hall', '3', 'Classroom'),
(roomID, 'BCH 302', 'Bagong Cabuyao Hall', '3', 'Classroom'),
(roomID, 'BCH 303', 'Bagong Cabuyao Hall', '3', 'Classroom'),
(roomID, 'Registrar', 'Main Building', '1', 'Office'),
(roomID, 'MISD', 'Main Building', '1', 'Office'),
(roomID, 'Computer Laboratory 1', 'Main Building', '1', 'Laboratory'),
(roomID, 'Computer Laboratory 4', 'Main Building', '2', 'Laboratory');

select * from borrowers;

insert into borrowers values
(borrowerID, 'Ian Daniel', 'Felonia', 'Student', '3205541', 'College of Computing Studies', '123456789', 'iandanielfelonia@gmail.com'),
(borrowerID, 'Khynan', 'Delmonte', 'Student', '3522422', 'College of Computing Studies', '975318642', 'khynandelmonte@gmail.com'),
(borrowerID, 'Kenneth Ruther', 'Sobrepena', 'Student', '4225541', 'College of Computing Studies', '96661234', 'sobrepenakenneth@gmail.com');

/*
CREATE TABLE IF NOT EXISTS inventory(
    equipmentID INT AUTO_INCREMENT PRIMARY KEY,
    propertyTag VARCHAR(30) NOT NULL UNIQUE,
    equipmentName VARCHAR(150) NOT NULL,
    categoryName VARCHAR(100) NOT NULL,
    brandModel VARCHAR(150),
    serialNumber VARCHAR(100),
    description VARCHAR(255) NOT NULL,
    roomID INT NOT NULL,
    purchaseDate DATE,
    purchaseCost DECIMAL(12, 2),
    condition_status VARCHAR(50) DEFAULT 'Good',
    availability VARCHAR(50) DEFAULT 'Available',
    date_added DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoryName) REFERENCES equipment_categories(categoryName)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (roomID) REFERENCES rooms_offices(roomID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idxAvailability (availability)
);
*/
insert into inventory values 
(equipmentID, '99103', 'Widescreen TV', 'Electronics', 'LG', '019993912', 'A widescren TV', null, '2023-05-12', '15000', 'Good', availability, '2023-05-14');

select * from inventory;
/* custodianID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    position VARCHAR(100),
    department VARCHAR(100),
    contactNumber VARCHAR(20)
    */
insert into custodians values (custodianID, 'Ad', 'Min', 'Admin', 'IT', 'du'), (custodianID, 'Cas', 'Todian', 'Custodian', 'IT', 'e');
insert into userAccounts values (1, 'admin', password('admin'), true), (2, 'user', password('user'), false);

select * from custodians;

select * from borrow_transaction;