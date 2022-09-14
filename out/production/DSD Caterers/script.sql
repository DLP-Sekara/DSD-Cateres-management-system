DROP DATABASE IF EXISTS CateringService;
CREATE DATABASE IF NOT EXISTS CateringService;
SHOW DATABASES ;
USE CateringService;
#-------------------
DROP TABLE IF EXISTS Employee;
CREATE TABLE IF NOT EXISTS Employee(
    nic VARCHAR(20),
    employeeName VARCHAR(45) NOT NULL DEFAULT 'Unknown',
    address VARCHAR(20),
    contact VARCHAR(20),
    salary DOUBLE DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (nic)
    );
SHOW TABLES ;
DESCRIBE Employee;
#---------------------
DROP TABLE IF EXISTS SystemUser;
CREATE TABLE IF NOT EXISTS SystemUser(
    sid VARCHAR(15),
    userName VARCHAR(45) NOT NULL DEFAULT 'Unknown',
    password VARCHAR(15),
    CONSTRAINT PRIMARY KEY (sid)
    );
SHOW TABLES ;
DESCRIBE SystemUser;
#---------------------
DROP TABLE IF EXISTS Customer;
CREATE TABLE IF NOT EXISTS Customer(
    cid VARCHAR(15),
    customerName VARCHAR(45) NOT NULL DEFAULT 'Unknown',
    address VARCHAR(20),
    factory VARCHAR(20),
    contact VARCHAR(20),
    gender VARCHAR(15),
    sid VARCHAR(15),
    CONSTRAINT PRIMARY KEY (cid),
    CONSTRAINT FOREIGN KEY (sid) REFERENCES SystemUser(sid)
    );
SHOW TABLES ;
DESCRIBE Customer;
#---------------------
DROP TABLE IF EXISTS Driver;
CREATE TABLE IF NOT EXISTS Driver(
    driverId VARCHAR(15),
    driverName VARCHAR(15),
    driverContact VARCHAR(15),
    vehicleType VARCHAR(15),
    CONSTRAINT PRIMARY KEY (driverId)
    );
SHOW TABLES ;
DESCRIBE Driver;
#------------------------

DROP TABLE IF EXISTS Transport;
CREATE TABLE IF NOT EXISTS Transport(
    transportNo VARCHAR(15) NOT NULL,
    transportType VARCHAR(15),
    date VARCHAR(15),
    time VARCHAR(15),
    cost DOUBLE DEFAULT 0.00,
    driverId VARCHAR(15),
    CONSTRAINT PRIMARY KEY (transportNo),
    CONSTRAINT FOREIGN KEY (driverId) REFERENCES Driver(driverId)
    ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE Transport;

#------------------------


DROP TABLE IF EXISTS Orders;
CREATE TABLE IF NOT EXISTS Orders(
    oid VARCHAR(15),
    date VARCHAR(15),
    time VARCHAR(15),
    mealType VARCHAR(15),
    amount DOUBLE,
    cid VARCHAR(15),
    transportNo VARCHAR(15),
    CONSTRAINT PRIMARY KEY (oid),
    CONSTRAINT FOREIGN KEY (cid) REFERENCES Customer(cid)
    ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (transportNo) REFERENCES Transport(transportNo)
    );
SHOW TABLES ;
DESCRIBE Orders;
#-----------------------
DROP TABLE IF EXISTS Meal;
CREATE TABLE IF NOT EXISTS Meal(
    mealNo VARCHAR(15),
    mealName VARCHAR(15),
    mealType VARCHAR(15),
    cost DOUBLE DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (mealNo)
    );
SHOW TABLES ;
DESCRIBE Meal;

#------------------------
DROP TABLE IF EXISTS `Order Detail`;
CREATE TABLE IF NOT EXISTS `Order Detail`(
    mealNo VARCHAR(15),
    oid VARCHAR(15),
    breakfast INT,
    lunch INT,
    dinner INT,
    price DOUBLE,
    mealTime VARCHAR(15),
    CONSTRAINT FOREIGN KEY (mealNo) REFERENCES Meal(mealNo ),
    CONSTRAINT FOREIGN KEY (oid) REFERENCES Orders(oid)
    ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE `Order Detail`;

#-----------------------
DROP TABLE IF EXISTS Supplier;
CREATE TABLE IF NOT EXISTS Supplier(
    shopNo VARCHAR (15),
    shopName VARCHAR(15),
    address VARCHAR(20),
    contact VARCHAR(20),
    payment DOUBLE DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (shopNo)
    );
SHOW TABLES ;
DESCRIBE Supplier;

#------------------------

DROP TABLE IF EXISTS Ingredient;
CREATE TABLE IF NOT EXISTS Ingredient(
    mealNo VARCHAR(15),
    shopNo VARCHAR(15),
    ingredientName VARCHAR(15),
    qty INT,
    price DOUBLE,
    CONSTRAINT FOREIGN KEY (mealNo) REFERENCES Meal(mealNo ),
    CONSTRAINT FOREIGN KEY (shopNo) REFERENCES Supplier(shopNo)
    );
SHOW TABLES ;
DESCRIBE Ingredient;
#-----------------------
