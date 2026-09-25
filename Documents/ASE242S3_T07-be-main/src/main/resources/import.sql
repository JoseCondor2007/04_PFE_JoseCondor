/* =========================================================
   1) RECREAR BASE DE DATOS
   ========================================================= */
   USE  master;
   GO
IF DB_ID('RestuaranteDB') IS NOT NULL
    DROP DATABASE RestuaranteDB;
GO
CREATE DATABASE RestuaranteDB;
GO
USE RestuaranteDB;
GO

/* =========================================================
   2) TABLAS
   ========================================================= */

-- Employees (extra, como en tu diseño)
CREATE TABLE Employees (
    id_employee INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    position VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    document_type VARCHAR(20) NOT NULL,
    document_number VARCHAR(15) NOT NULL UNIQUE,
    is_active CHAR(1) NOT NULL DEFAULT('A'),
    CONSTRAINT CK_Employees_active CHECK (is_active IN ('A','I'))
);
GO

-- =================== MAESTRA ===================
CREATE TABLE Customers (
    id_customer INT IDENTITY(1,1) PRIMARY KEY,                -- PK
    name VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    documento_identificac VARCHAR(10) NOT NULL DEFAULT('DNI'),-- DEFAULT
    document_number VARCHAR(15) NOT NULL UNIQUE,              -- UNIQUE
    email VARCHAR(255) NOT NULL UNIQUE,                       -- UNIQUE
    phone CHAR(9) NOT NULL,
    status CHAR(1) NOT NULL DEFAULT('A'),                     -- DEFAULT (A/I)
    location VARCHAR(255) NOT NULL,
    created_at DATE NOT NULL DEFAULT(CONVERT(date, GETDATE())),-- FECHA (solo día)
    CONSTRAINT CK_Customers_status CHECK (status IN ('A','I')) -- CHECK
);
GO

-- Dishes (extra)
CREATE TABLE Dishes (
    id_dishe INT IDENTITY(1,1) PRIMARY KEY,
    diche_name VARCHAR(100) NOT NULL,
    category VARCHAR(10) NOT NULL,
    dishe_type VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status CHAR(1) NOT NULL DEFAULT('A'),
    CONSTRAINT CK_Dishes_price  CHECK (price > 0),
    CONSTRAINT CK_Dishes_status CHECK (status IN ('A','I'))
);
GO

-- Tables (extra)
CREATE TABLE Tables (
    id_tables INT IDENTITY(1,1) PRIMARY KEY,
    table_number INT NOT NULL UNIQUE,
    capacity INT NOT NULL,
    status CHAR(1) NOT NULL DEFAULT('A'),
    CONSTRAINT CK_Tables_capacity CHECK (capacity > 0),
    CONSTRAINT CK_Tables_status   CHECK (status IN ('A','I'))
);
GO

-- ============== TRANSACCIONAL: CABECERA ==============
CREATE TABLE Reservations (
    id_reservation INT IDENTITY(1,1) PRIMARY KEY,
    table_id INT NOT NULL,
    reservation_datetime DATE NOT NULL DEFAULT(CONVERT(date, GETDATE())), -- FECHA (solo día)
    number_of_guests INT NOT NULL CHECK (number_of_guests > 0),
    location VARCHAR(255) NOT NULL,
    payment_method VARCHAR(20) NOT NULL DEFAULT('Cash'),
    reservation_status VARCHAR(20) NOT NULL DEFAULT('Pending'),
    total_price DECIMAL(10,2) NOT NULL DEFAULT(0) CHECK (total_price >= 0),
    paid BIT NOT NULL DEFAULT(0),                                         -- BOOLEANO (boole(t)ano)
    status CHAR(1) NOT NULL DEFAULT('A') CHECK (status IN ('A','I')),     -- estado lógico
    created_at DATE NOT NULL DEFAULT(CONVERT(date, GETDATE())),           -- FECHA (solo día)
    Customers_id_customer INT NOT NULL,
    CONSTRAINT FK_Reservations_Customers FOREIGN KEY (Customers_id_customer)
        REFERENCES Customers(id_customer),
    CONSTRAINT FK_Reservations_Tables FOREIGN KEY (table_id)
        REFERENCES Tables(id_tables),
    CONSTRAINT CK_Reservations_state CHECK (reservation_status IN ('Pending','Confirmed','Cancelled'))
);
GO

-- ============== TRANSACCIONAL: DETALLE ==============
CREATE TABLE Reservation_Detail (
    id_reservation_detail INT IDENTITY(1,1) PRIMARY KEY,
    id_reservation INT NOT NULL,
    mesa_num INT NOT NULL CHECK (mesa_num > 0),
    status CHAR(1) NOT NULL DEFAULT('A') CHECK (status IN ('A','I')),     -- estado lógico
    created_at DATE NOT NULL DEFAULT(CONVERT(date, GETDATE())),           -- FECHA (solo día)
    CONSTRAINT FK_ReservationDetail_Reservation FOREIGN KEY (id_reservation)
        REFERENCES Reservations(id_reservation),
    CONSTRAINT UQ_ResDet UNIQUE (id_reservation, mesa_num)                -- evita mesa duplicada
);
GO

-- "Order" (extra, igual que tu diseño, con fecha DATE y estado lógico)
CREATE TABLE "Order" (
    Id_order INT IDENTITY(1,1) PRIMARY KEY,
    Employees_id_employee INT NOT NULL,
    Customers_id_customer INT NOT NULL,
    Tables_id_tables INT NOT NULL,
    order_datetime DATE NOT NULL DEFAULT(CONVERT(date, GETDATE())),       -- FECHA (solo día)
    order_status VARCHAR(20) NOT NULL DEFAULT('Pending'),
    total_amount DECIMAL(10,2) NOT NULL DEFAULT(0) CHECK (total_amount >= 0),
    costumer_name VARCHAR(100) NOT NULL,
    Employee_name VARCHAR(100) NOT NULL,
    status CHAR(1) NOT NULL DEFAULT('A') CHECK (status IN ('A','I')),
    CONSTRAINT FK_Order_Employees FOREIGN KEY (Employees_id_employee)
        REFERENCES Employees(id_employee),
    CONSTRAINT FK_Order_Customers FOREIGN KEY (Customers_id_customer)
        REFERENCES Customers(id_customer),
    CONSTRAINT FK_Order_Tables FOREIGN KEY (Tables_id_tables)
        REFERENCES Tables(id_tables),
    CONSTRAINT CK_Order_status CHECK (order_status IN ('Pending','Completed','Cancelled'))
);
GO

CREATE TABLE Order_Detail (
    id_order_detail INT IDENTITY(1,1) PRIMARY KEY,
    Dishes_id_dishe INT NOT NULL,
    Order_Id_order INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),
    status CHAR(1) NOT NULL DEFAULT('A') CHECK (status IN ('A','I')),
    CONSTRAINT FK_OrderDetail_Dishes FOREIGN KEY (Dishes_id_dishe)
        REFERENCES Dishes(id_dishe),
    CONSTRAINT FK_OrderDetail_Order FOREIGN KEY (Order_Id_order)
        REFERENCES "Order"(Id_order)
);
GO

/* =========================================================
   3) INSERCIÓN DE DATOS (maestra y transaccionales)
   ========================================================= */

-- Employees
INSERT INTO Employees (name, lastname, position, email, document_type, document_number)
VALUES
('Araceli', 'Chumacero', 'Manager', 'araceli@example.com', 'DNI', '12345678'),
('Juan', 'Perez', 'Chef',    'juan@example.com',  'DNI', '87654321'),
('Luisa','Gomez', 'Waiter',  'luisa@example.com', 'DNI', '11223344');

-- Customers (MAESTRA)
INSERT INTO Customers (name, lastname, documento_identificac, document_number, email, phone, location)
VALUES
('Carlos','Ramirez','DNI','11223344','carlos@example.com','987654321','Lima'),
('Maria','Lopez','DNI','22334455','maria@example.com','987654322','Cusco'),
('Pedro','Sanchez','DNI','33445566','pedro@example.com','987654323','Arequipa');


-- Tables
INSERT INTO Tables (table_number, capacity) VALUES (1,4),(2,2),(3,6);

-- Reservations (TRANSACCIONAL CABECERA)
INSERT INTO Reservations (table_id, reservation_datetime, number_of_guests, location, payment_method, reservation_status, total_price, paid, Customers_id_customer)
VALUES
(1, CONVERT(date, GETDATE()), 2, 'Lima',  'Card', 'Confirmed', 50.00, 1, 1),
(2, CONVERT(date, GETDATE()), 4, 'Cusco', 'Cash', 'Pending',   80.00, 0, 2);

-- Reservation_Detail (TRANSACCIONAL DETALLE)
INSERT INTO Reservation_Detail (id_reservation, mesa_num) VALUES
(1,1),(1,2),(2,3);

-- Dishes y Orders (extra de tu diseño)
INSERT INTO Dishes (diche_name, category, dishe_type, description, price) VALUES
('Pizza Margherita','Food','Main','Pizza clásica con tomate y queso',25.50),
('Spaghetti Bolognese','Food','Main','Pasta con salsa boloñesa',30.00),
('Ensalada Cesar','Food','Appetizer','Ensalada con pollo y aderezo Cesar',18.00);

INSERT INTO "Order" (Employees_id_employee, Customers_id_customer, Tables_id_tables, order_status, total_amount, costumer_name, Employee_name)
VALUES
(1,1,1,'Completed',50.00,'Carlos Ramirez','Araceli Chumacero'),
(2,2,2,'Pending',  80.00,'Maria Lopez','Juan Perez');

INSERT INTO Order_Detail (Dishes_id_dishe, Order_Id_order, quantity, subtotal) VALUES
(1,1,2,51.00),(2,2,1,30.00),(3,1,1,18.00);
GO

/* =========================================================
   4) ACTUALIZACIONES (maestra y transaccionales)
   ========================================================= */

-- MAESTRA: cambiar estado lógico (eliminación lógica)
UPDATE Customers SET status = 'I' WHERE id_customer = 3;

-- TRANSACCIONALES:
UPDATE Reservations SET reservation_status = 'Confirmed', paid = 1 WHERE id_reservation = 2;
UPDATE Reservation_Detail SET status = 'I' WHERE id_reservation = 2 AND mesa_num = 3;
GO

/* =========================================================
   5) LISTADOS (maestra y transaccionales)
   ========================================================= */

-- Maestra: Customers
SELECT id_customer, name, lastname, documento_identificac, document_number, email,
       phone, status, location, created_at
FROM Customers;

-- Transaccional cabecera: Reservations + nombre cliente
SELECT r.id_reservation, r.reservation_datetime, r.number_of_guests, r.location,
       r.payment_method, r.reservation_status, r.total_price, r.paid, r.status,
       c.name + ' ' + c.lastname AS CustomerName
FROM Reservations r
JOIN Customers c ON c.id_customer = r.Customers_id_customer
ORDER BY r.id_reservation;

-- Transaccional detalle: Reservation_Detail
SELECT id_reservation_detail, id_reservation, mesa_num, status, created_at
FROM Reservation_Detail
ORDER BY id_reservation, id_reservation_detail;


select * from Customers