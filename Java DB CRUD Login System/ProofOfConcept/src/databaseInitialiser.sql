drop database if exists ProofOfConcept;
create database ProofOfConcept;

use ProofOfConcept;

create table employeeList (

employeeName varchar(100) unique not null,
pass varchar(100) unique not null

);


create table Borrowers (

	borrowerID int auto_increment primary key,
    borrowerName varchar(100) unique not null

);

create table EquipmentStock(

equipID int auto_increment primary key,
equipName varchar(100) unique not null,
equipStock int check(equipStock >= 0)

);

create table BorrowRecord(
recordID int auto_increment primary key,
borrowerID int not null,
borrowedEquip int not null,
borrowDate date not null,
isReturned bool,

foreign key (borrowerID) references Borrowers(borrowerID),
foreign key (borrowedEquip) references EquipmentStock(equipID)
);
insert into Borrowers values (borrowerID, 'Bobbit'), (borrowerID, 'Dustin');
insert into EquipmentStock values (equipID, 'Wooden Stick', 5);

insert into employeeList values
('Sir Normalot', 'notsecurepassword'),
('Peregrin Took', 'secondbreakfast');

drop user if exists 'authenticator'@'localhost';
flush PRIVILEGES;
CREATE USER 'authenticator'@'localhost' identified by 'root';
grant select on employeeList to 'authenticator'@'localhost';
flush PRIVILEGES;

CREATE USER 'employee'@'localhost' identified by 'employee';
GRANT select, insert on BorrowRecord to 'employee'@'localhost';
flush privileges;

drop table BorrowRecord;

select * from BorrowRecord;