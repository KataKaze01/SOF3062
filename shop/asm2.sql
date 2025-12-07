-- =============================================
-- CREATE DATABASE SHOP
-- =============================================

-- USE master;
-- GO
-- DROP DATABASE IF EXISTS shop;
-- GO
 CREATE DATABASE shop;
 GO

USE shop;
GO

-- =============================================
-- DROP EXISTING TABLES (nếu cần)
-- =============================================
IF OBJECT_ID('order_items', 'U') IS NOT NULL DROP TABLE order_items;
IF OBJECT_ID('orders', 'U') IS NOT NULL DROP TABLE orders;
IF OBJECT_ID('user_roles', 'U') IS NOT NULL DROP TABLE user_roles;
IF OBJECT_ID('users', 'U') IS NOT NULL DROP TABLE users;
IF OBJECT_ID('roles', 'U') IS NOT NULL DROP TABLE roles;
IF OBJECT_ID('products', 'U') IS NOT NULL DROP TABLE products;
GO

-- =============================================
-- CREATE TABLES
-- =============================================

-- Bảng Roles
CREATE TABLE roles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) UNIQUE NOT NULL -- e.g., "ROLE_USER", "ROLE_ADMIN"
);
GO

-- Bảng Users
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    email NVARCHAR(255) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20),
    address NVARCHAR(500),
    created_at DATETIME2 DEFAULT GETDATE()
);
GO

-- Bảng liên kết User - Role
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT FK_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT FK_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
GO

-- Bảng Products
CREATE TABLE products (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(500) NOT NULL,
    description NVARCHAR(MAX),
    image_url NVARCHAR(500),
    price DECIMAL(19,2) NOT NULL,
    stock INT DEFAULT 0,
    category NVARCHAR(255), -- e.g., "Sách Trong Nước", "Đồ Chơi"
    created_at DATETIME2 DEFAULT GETDATE()
);
GO

-- Bảng Orders
CREATE TABLE orders (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status NVARCHAR(50) DEFAULT N'Đã đặt',
    order_date DATETIME2 DEFAULT GETDATE(),
    total_amount DECIMAL(19,2),
    CONSTRAINT FK_orders_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
GO

-- Bảng Order Items (chi tiết đơn hàng)
CREATE TABLE order_items (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    price DECIMAL(19,2) NOT NULL, -- Giá tại thời điểm đặt hàng
    CONSTRAINT FK_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT FK_order_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);
GO

CREATE TABLE cart_items (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
go

-- =============================================
-- INSERT DATA MẪU
-- =============================================

-- Insert Roles
INSERT INTO roles (name) VALUES (N'ROLE_USER'), (N'ROLE_ADMIN');
GO

-- Insert Admin User (mật khẩu: admin123 đã mã hóa bằng BCrypt)
INSERT INTO users (name, email, password, phone, address)
VALUES (N'Admin Fahasa', N'admin@fahasa.com', N'$2a$10$D4xgJ8yT9r4KZv1v0vQj6eN3jKqJqXqQqXqQqXqQqXq', N'0900000000', N'TP. HCM');
GO

-- Gán ROLE_ADMIN cho Admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.email = N'admin@fahasa.com' AND r.name = N'ROLE_ADMIN';
GO

-- Insert một số sản phẩm mẫu
INSERT INTO products (name, description, image_url, price, stock, category)
VALUES
(N'Breath - Hơi Thở Nối Dài Sự Sống', N'Sách về hơi thở...', N'/images/book1.png', 202500.00, 50, N'Sách Trong Nước'),
(N'Cây Thông Noel 150cm', N'Cây thông phủ tuyết...', N'/images/christmas1.png', 1240000.00, 10, N'Giáng Sinh'),
(N'Con Quay Yoyo Ngọn Lửa Bùng Cháy', N'Đồ chơi quay...', N'/images/toy1.png', 424500.00, 30, N'Đồ Chơi');
GO

-- Insert một đơn hàng mẫu
INSERT INTO orders (user_id, status, total_amount)
SELECT TOP 1 u.id, N'Đang giao', 627000.00 FROM users u WHERE u.email = N'admin@fahasa.com';
GO

-- Insert chi tiết đơn hàng
INSERT INTO order_items (order_id, product_id, quantity, price)
SELECT o.id, p.id, 1, p.price
FROM orders o, products p
WHERE o.user_id = (SELECT id FROM users WHERE email = N'admin@fahasa.com')
AND p.name = N'Breath - Hơi Thở Nối Dài Sự Sống';
GO

-- src/main/resources/data.sql
SELECT * FROM users WHERE email = 'admin@fahasa.com';
UPDATE users SET password = '123' WHERE email = 'fanmu2122@gmail.com';
SELECT id, name, email, password FROM users WHERE email = 'fanmu2122@gmail.com';

-- =============================================
-- DONE
-- =============================================
PRINT 'Database shop đã được tạo thành công!';
INSERT INTO roles (name) VALUES ('ROLE_USER');
SELECT u.id, u.name, u.email, u.password, r.name AS role_name
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
WHERE u.email = 'admin@fahasa.com';
-- Giả sử ID của user là 2, ID của ROLE_USER là 1
INSERT INTO user_roles (user_id, role_id) VALUES (2, 1);
SELECT u.id, u.name, u.email, r.name AS role_name
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
WHERE u.email = 'fanmu2122@gmail.com';
ALTER TABLE users
ADD reset_token NVARCHAR(255);

SELECT * FROM cart_items;