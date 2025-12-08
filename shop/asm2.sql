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

-- Tạo bảng roles
CREATE TABLE roles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) UNIQUE NOT NULL
);

-- Tạo bảng users
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    email NVARCHAR(255) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20),
    address NVARCHAR(500),
    created_at DATETIME2 DEFAULT GETDATE()
);

-- Tạo bảng user_roles (liên kết nhiều-nhiều)
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Tạo bảng products
CREATE TABLE products (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(500) NOT NULL,
    description TEXT,
    image_url NVARCHAR(500),
    price DECIMAL(19,2) NOT NULL,
    stock INT DEFAULT 0,
    category NVARCHAR(255) NOT NULL
);

-- Tạo bảng orders
CREATE TABLE orders (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date DATETIME2 DEFAULT GETDATE(),
    total_amount DECIMAL(19,2),
    status NVARCHAR(50) DEFAULT N'Đang xử lý',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tạo bảng order_items
CREATE TABLE order_items (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(19,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);go

-- Thêm dữ liệu mẫu
INSERT INTO roles (name) VALUES (N'ROLE_USER'), (N'ROLE_ADMIN');

INSERT INTO users (name, email, password, phone, address) 
VALUES (N'Admin Fahasa', N'admin@fahasa.com', N'admin123', N'0900000000', N'TP. HCM');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 2); -- Gán ROLE_ADMIN cho admin

INSERT INTO products (name, price, image_url, category, stock)
VALUES 
(N'Breath - Hơi Thở Nối Dài Sự Sống', 202500.00, '/images/book1.png', N'Sách Trong Nước', 50),
(N'Con Quay Yoyo Ngọn Lửa Bùng Cháy', 424500.00, '/images/toy1.png', N'Đồ Chơi', 30);

SELECT id, name, price, stock FROM products;

SELECT * FROM products;

INSERT INTO products (name, price, image_url, category, stock) VALUES
('Tinh Dầu Thơm Phòng Harmony Home 50 ml - Vietnamese Coffee', 216000, '/images/LDSK/beauty1.png', 'Làm Đẹp - Sức Khỏe', 100),
('Tinh Dầu Thơm Phòng Harmony Home 50 ml - Peach Bloom', 216000, '/images/LDSK/beauty2.png', 'Làm Đẹp - Sức Khỏe', 100),
( 'Tinh Dầu Thơm Phòng Harmony Home 120 ml - Amber Wood', 391500, '/images/LDSK/beauty3.png', 'Làm Đẹp - Sức Khỏe', 100),
( 'Tinh Dầu Khuếch Tán Harmony Home 50 ml - Ocean Mint', 216000, '/images/LDSK/beauty4.png', 'Làm Đẹp - Sức Khỏe', 100),
( 'Tinh Dầu Khuếch Tán Harmony Home 50 ml - Sweet Dreams', 216000, '/images/LDSK/beauty5.png', 'Làm Đẹp - Sức Khỏe', 100),
( 'Khăn Giấy Ướt Vệ Sinh Tan Trong Nước - Deeyeo', 11500, '/images/LDSK/beauty6.png', 'Làm Đẹp - Sức Khỏe', 100),
( 'Hộp Khăn Lau Kính Clearwipe - Clearwipe Lens Cleaner', 35000, '/images/LDSK/beauty7.png', 'Làm Đẹp - Sức Khỏe', 100);

-- Insert 50 sản phẩm đồ chơi vào bảng products

-- Section: Đồ Chơi Nổi Bật (1-5)
INSERT INTO products (name, price, image_url, category, stock) VALUES
('Con Quay Yoyo Ngọn Lửa Bùng Cháy - Yoyo 22 EU677261R', 424500, '/images/giaodien3/toy1.png', 'Đồ Chơi',50),
('Thú Bông Capybara 25 cm - Nuan Nuan BKP030', 134500, '/images/giaodien3/toy2.png', 'Đồ Chơi',100),
('Robot Nắp Chai Bottleman BOT-09 - Fujinblack Phong Thần',164500 , '/images/giaodien3/toy3.png', 'Đồ Chơi',67),
('Trống Thả Hình Đa Năng - DK580001', 62500, '/images/giaodien3/toy4.png', 'Đồ Chơi', 100),
('Đồ Chơi Xe Biến Hình Hồ Thần Địa Ngục Hellfire Tiger', 209500, '/images/giaodien3/toy5.png', 'Đồ Chơi', 100);

-- Section: Đồ Chơi Mới Về (6-10)
INSERT INTO products (name, price, image_url, category, stock) VALUES
('Trading Card Game Chính Hãng: TCG Thẻ Bài Pokemon', 120000, '/images/giaodien3/toy6.png', 'Đồ Chơi', 100),
('Siêu Xe Hot Wheels C4982 - 76/250 - 76 Chevy Chevette', 62000, '/images/giaodien3/toy7.png', 'Đồ Chơi', 100),
('Siêu Xe Hot Wheels C4982 - 64/250 - Mercedes Benz Unimog', 62000,'/images/giaodien3/toy8.png', 'Đồ Chơi', 100),
('Siêu Xe Hot Wheels C4982 - 56/250 - 67 Camaro', 62000,'/images/giaodien3/toy9.png', 'Đồ Chơi', 100),
('Siêu Xe Hot Wheels C4982 - 69/250 - Volvo 240 Drift', 62000, '/images/giaodien3/toy10.png', 'Đồ Chơi', 100);

-- Section: Xe HOTWHEELS - Mẫu Mới (11-15)
INSERT INTO products (name, price, image_url, category, stock) VALUES
('Siêu Xe Hot Wheels C4982 - 213/250 - Lotus Cortina', 62000, '/images/giaodien3/toy11.png', 'Đồ Chơi', 100),
('Siêu Xe Hot Wheels C4982 - 22/250 - Bogzilla', 62000, '/images/giaodien3/toy12.png', 'Đồ Chơi', 100),
('Siêu Xe Hot Wheels C4982 - 36/250 - 70 Dodge Hemi', 62000,'/images/giaodien3/toy13.png', 'Đồ Chơi', 100),
('Siêu Xe Hot Wheels C4982 - 232/250 - 1966 Bat Boat', 62000, '/images/giaodien3/toy14.png', 'Đồ Chơi', 100),
('Đồ Chơi Mô Hình Siêu Xe - Hot Wheels C4982 - 147/250 - Spec', 62000, '/images/giaodien3/toy15.png', 'Đồ Chơi', 100);

-- Section: Đồ Chơi Hướng Nghiệp (16-20)
INSERT INTO products (name, price, image_url, category, stock) VALUES
('Đồ Chơi Trạm Xăng - Baoli 2012#B - Màu Xanh Dương', 531000, '/images/giaodien3/toy16.png', 'Đồ Chơi', 100),
('Đồ Chơi Trạm Xăng - Baoli 2012#R - Màu Đỏ', 531000,'/images/giaodien3/toy17.png', 'Đồ Chơi', 100),
('Bộ Đồ Chơi Bác Sĩ - Màu Xanh Có Đèn Và Âm Thanh', 178750, '/images/giaodien3/toy18.png', 'Đồ Chơi', 100),
('Đồ Chơi Bác Sĩ - Firstar DK81148 (19 Chi Tiết)', 169000, '/images/giaodien3/toy19.png', 'Đồ Chơi', 100),
('Đồ Chơi Bé Làm Bác Sĩ - Mini Kitchen 623', 71000,'/images/giaodien3/toy20.png', 'Đồ Chơi', 100);

-- Section: Thú Bông Fuggler (21-25)
INSERT INTO products (name, price, image_url, category, stock) VALUES
('Đồ Chơi Sưu Tầm Fuggler Baby Fuggs 3.5 Inch - Zuru Toys 1570 (1)', 170000, '/images/giaodien3/toy21.png', 'Đồ Chơi', 100),
('Đồ Chơi Sưu Tầm Fuggler Baby Fuggs 3.5 Inch - Zuru Toys 1570 (2)', 170100, '/images/giaodien3/toy22.png', 'Đồ Chơi', 100),
('Đồ Chơi Sưu Tầm Fuggler Baby Fuggs 3.5 Inch - Zuru Toys 1570 (3)', 170100, '/images/giaodien3/toy23.png', 'Đồ Chơi', 100),
('Đồ Chơi Sưu Tầm Fuggler Baby Fuggs 3.5 Inch - Zuru Toys 1570 (4)', 170100,'/images/giaodien3/toy24.png', 'Đồ Chơi', 100),
('Đồ Chơi Sưu Tầm Fuggler Baby Fuggs 3.5 Inch - Zuru Toys 1570 (5)', 170100, '/images/giaodien3/toy25.png', 'Đồ Chơi', 100);

-- Section: Đồ Chơi Sơ Sinh (26-30)
INSERT INTO products (name, price, image_url, category, stock) VALUES
('Đồ Chơi Vòng Xoay Đại Dương - Baoli 1803#P - Màu Hồng', 886500, '/images/giaodien3/toy26.png', 'Đồ Chơi', 100),
('Đồ Chơi Vòng Xoay Đại Dương - Baoli 1803#B - Màu Xanh Dương', 886500, '/images/giaodien3/toy27.png', 'Đồ Chơi', 100),
('Tàu Hỏa Học Tập Đa Năng - Baoli 1909#P - Màu Hồng', 792000, '/images/giaodien3/toy28.png', 'Đồ Chơi', 100),
('Tàu Hỏa Học Tập Đa Năng - Baoli 1909#G - Màu Xanh Lá', 792000,  '/images/giaodien3/toy29.png', 'Đồ Chơi', 100),
('Đồ Chơi TV Âm Nhạc - Baoli 1501#B - Màu Xanh Dương', 252000,'/images/giaodien3/toy30.png', 'Đồ Chơi', 100);

-- Section: Lắp Ráp Đơn Giản (31-35)
INSERT INTO products (name, price, image_url, category, stock) VALUES
('Đồ Chơi Lắp Ráp 12 Con Giáp - Starmerry 6563 (SM198B) - Pig', 14000,'/images/giaodien3/toy31.png', 'Đồ Chơi', 100),
('Đồ Chơi Lắp Ráp Mô Hình Vũ Trụ Brainrot - J9990 - 05 (42 Mảnh)', 53000,'/images/giaodien3/toy32.png', 'Đồ Chơi', 100),
('Đồ Chơi Lắp Ráp DIY - Cresta DK81087 - Khủng Long Bạo Chúa', 71500, '/images/giaodien3/toy33.png', 'Đồ Chơi', 100),
('Đồ Chơi Lắp Ráp Na Tra 2 - Ma Đồng Náo Hải - LY43 - Mẫu 8', 35000,'/images/giaodien3/toy34.png', 'Đồ Chơi', 100),
('Bộ 2 Mô Hình Khủng Long Lắp Ráp DIY 2001-1', 80000, '/images/giaodien3/toy35.png', 'Đồ Chơi', 100);

-- Section: Đồ Chơi Gỗ (36-40)
INSERT INTO products (name, price, image_url, category, stock) VALUES
('Đồ Chơi Ghép Gỗ Cún Yêu - TP-3216', 44000,  '/images/giaodien3/toy36.png', 'Đồ Chơi', 100),
('Đồ Chơi Ghép Gỗ Tàu Thủy - TP-3261', 44000, '/images/giaodien3/toy37.png', 'Đồ Chơi', 100),
('Đồ Chơi Gỗ Woody - Tìm Cặp Đôi Gỗ', 270000, '/images/giaodien3/toy38.png', 'Đồ Chơi', 100),
('Đồ Chơi Ghép Gỗ Éch Xanh - TP-3223', 44000, '/images/giaodien3/toy39.png', 'Đồ Chơi', 100),
('Đồ Chơi Ghép Gỗ Động Vật Biển - TP-3186', 62000,'/images/giaodien3/toy40.png', 'Đồ Chơi', 100);

-- Section: Mô Hình Động Vật (41-45)
INSERT INTO products (name, price, image_url, category, stock) VALUES
('Đồ Chơi Mô Hình Khủng Long Spinosaurus 12 Inch Phiên Bản Đặc Biệt', 296100, '/images/giaodien3/toy41.png', 'Đồ Chơi', 100),
('Đồ Chơi Quả Trứng Tiền Sử - Động Vật - Cresta DK81391', 71500,'/images/giaodien3/toy42.png', 'Đồ Chơi', 100),
('Đồ Chơi Quả Trứng Tiền Sử - Khủng Long - Cresta DK81392', 71500, '/images/giaodien3/toy43.png', 'Đồ Chơi', 100),
('Đồ Chơi Mẹ Con Gà Nhà Tung Tặng - 7539 (1107A)', 54500, '/images/giaodien3/toy44.png', 'Đồ Chơi', 100),
('Đồ Chơi Mô Hình 12 Sinh Vật Biển CollectA -1 - CollectA A1107', 269000, '/images/giaodien3/toy45.png', 'Đồ Chơi', 100);

-- Section: Mô Hình Máy Bay (46-50)
INSERT INTO products (name, price, image_url, category, stock) VALUES
('Đồ Chơi Mô Hình Máy Bay Airport Vehicles - Majorette 8503001000', 179000, '/images/giaodien3/toy46.png', 'Đồ Chơi', 100),
('Đồ Chơi Máy Bay Đụng Biến Hình Khủng Long - Kingdom Toys 747 (1)', 84500, '/images/giaodien3/toy47.png', 'Đồ Chơi', 100),
('Đồ Chơi Máy Bay Đụng Biến Hình Khủng Long - Kingdom Toys 747 (2)', 84500,'/images/giaodien3/toy48.png', 'Đồ Chơi', 100),
('Đồ Chơi Mô Hình Máy Bay Thương Mại Có Đèn Và Âm Thanh (1)', 179000,'/images/giaodien3/toy49.png', 'Đồ Chơi', 100),
('Đồ Chơi Mô Hình Máy Bay Thương Mại Có Đèn Và Âm Thanh (2)', 179000,'/images/giaodien3/toy50.png', 'Đồ Chơi', 100);

-- Xác nhận insert thành công
SELECT 'Đã insert thành công 50 sản phẩm đồ chơi!' AS status;

-- Insert 16 sản phẩm Sách Trong Nước vào bảng products

INSERT INTO products (name, price, image_url, category, stock) VALUES
('Phong Thủy Trong Xây Sửa Nhà - Khơi Dậy Năng Lượng', 347400, '/images/STN/book1.png', 'Sách Trong Nước', 100),
('Học Như Một Chuyên Gia - Áp Dụng Các Công Cụ Trên N', 107100, '/images/STN/book2.png', 'Sách Trong Nước', 100),
('Đời Gió Bụi', 216000, '/images/STN/book3.png', 'Sách Trong Nước', 100),
('Đời Gió Bụi - Bìa Cứng - Kèm Chữ Ký Tác Giả', 315000, '/images/STN/book4.png', 'Sách Trong Nước', 100),
('Vì Sao Càng Yêu Càng Có Đơn', 169200, '/images/STN/book5.png', 'Sách Trong Nước', 100),
('Tại Sao Chúng Ta Ăn Quá Nhiều', 234000, '/images/STN/book6.png', 'Sách Trong Nước', 100),
('Hạt Từ Thần', 265500, '/images/STN/book7.png', 'Sách Trong Nước', 100),
('Flashcard - Bộ Thẻ Học Ghép Văn Tiếng Việt - Chữ Cái', 36000, '/images/STN/book8.png', 'Sách Trong Nước', 100),
('Flashcard - Bộ Thẻ Thông Minh - Bé Học Toán (1-6 Tuổi)', 36000, '/images/STN/book9.png', 'Sách Trong Nước', 100),
('Những Ngã Tư Và Những Cột Đèn (Tái Bản 2025)', 214200, '/images/STN/book10.png', 'Sách Trong Nước', 100),
('Con Nhà Văn - Thiếu Nhi', 135000, '/images/STN/book11.png', 'Sách Trong Nước', 100),
('Mắt Sáng Gin Giữ Tình Hoa', 135000, '/images/STN/book12.png', 'Sách Trong Nước', 100),
('Con Nhà Văn - Nhập Môn', 144000, '/images/STN/book13.png', 'Sách Trong Nước', 100),
('Con Nhà Văn - Lập Thần', 162000, '/images/STN/book14.png', 'Sách Trong Nước', 100),
('Con Nhà Văn - Cầm Bút', 175500, '/images/STN/book15.png', 'Sách Trong Nước', 100),
('Gấp Một Miếng Việt - Bìa Cứng', 134100, '/images/STN/book16.png', 'Sách Trong Nước', 100);

-- Xác nhận insert thành công
SELECT 'Đã insert thành công 16 sản phẩm Sách Trong Nước!' AS status;

-- Insert 12 sản phẩm Foreign Books vào bảng products

INSERT INTO products (name, price, image_url, category, stock) VALUES
('Sunrise On The Reaping: Collector''s Gift Edition (The...', 777000, '/images/FB/foreign1.png', 'Foreign Books', 100),
('The Defender', 263000, '/images/FB/foreign2.png', 'Foreign Books', 100),
('Perspectives Advanced With The Spark Platform', 398000, '/images/FB/foreign3.png', 'Foreign Books', 100),
('Perspectives Upper Intermediate With The Spark Platform', 398000, '/images/FB/foreign4.png', 'Foreign Books', 100),
('Perspectives Pre-Intermediate With The Spark Platform', 398000, '/images/FB/foreign5.png', 'Foreign Books', 100),
('Perspectives Intermediate With The Spark Platform', 398000, '/images/FB/foreign6.png', 'Foreign Books', 100),
('Modern Semiconductor Devices For Integrated Circuits (1St...', 6183000, '/images/FB/foreign7.png', 'Foreign Books', 100),
('Illustrator Expo', 670000, '/images/FB/foreign8.png', 'Foreign Books', 100),
('Christmas Wakuwaku Santa', 335000, '/images/FB/foreign9.png', 'Foreign Books', 100),
('Food 2025', 479000, '/images/FB/foreign10.png', 'Foreign Books', 100),
('Satsuki To Mei No Ie No Tsukuri', 274000, '/images/FB/foreign11.png', 'Foreign Books', 100),
('Yasuno Moyoko Senshu Shugashugaru', 455000, '/images/FB/foreign12.png', 'Foreign Books', 100);

-- Xác nhận insert thành công
SELECT 'Đã insert thành công 12 sản phẩm Foreign Books!' AS status;

-- Insert 12 sản phẩm VPP - DCHS Theo Thương Hiệu vào bảng products

INSERT INTO products (name, price, image_url, category, stock) VALUES
('Lịch Bàn Thị Trấn Mèo 2026', 54000, '/images/DCHS/vpp1.png', 'VPP - DCHS', 100),
('Hộp 20 Tuýp Màu Vẽ Acrylic 20 ml - Colormate 114919', 145800, '/images/DCHS/vpp2.png', 'VPP - DCHS', 100),
('Hộp 16 Chai Màu Vẽ Pastel Acrylic 20 ml - Colormate 115046', 154800, '/images/DCHS/vpp3.png', 'VPP - DCHS', 100),
('Vỉ 10 Bút Chi HB Zootopia - iiGEN YZ360035', 45000, '/images/DCHS/vpp4.png', 'VPP - DCHS', 100),
('Sổ Lò Xo Made With Love A6 - Kẻ Ngang - 210 Trang', 32000, '/images/DCHS/vpp5.png', 'VPP - DCHS', 100),
('Bút Chi Bấm 0.5 mm Kèm Tẩy Và Ngòi Zootopia - iiGEN YZ360050 (Mẫu 1)', 45000, '/images/DCHS/vpp6.png', 'VPP - DCHS', 100),
('Bút Chi Bấm 0.5 mm Kèm Tẩy Và Ngòi Zootopia - iiGEN YZ360050 (Mẫu 2)', 45000, '/images/DCHS/vpp7.png', 'VPP - DCHS', 100),
('Bút Chi Bấm 0.5 mm Zootopia - iiGEN YZ360050 (Mẫu 1)', 27000, '/images/DCHS/vpp8.png', 'VPP - DCHS', 100),
('Ngòi Bút Chi 2B 0.5 mm Zootopia - iiGEN YZ360052 (Mẫu 1)', 18000, '/images/DCHS/vpp9.png', 'VPP - DCHS', 100),
('Bút Chi Bấm 0.5 mm Zootopia - iiGEN YZ360050 (Mẫu 2)', 27000, '/images/DCHS/vpp10.png', 'VPP - DCHS', 100),
('Kéo Học Sinh Zootopia - iiGEN YZ360056 - Nick Wilde', 27000, '/images/DCHS/vpp11.png', 'VPP - DCHS', 100),
('Ngòi Bút Chi 2B 0.5 mm Zootopia - iiGEN YZ360052 (Mẫu 2)', 18000, '/images/DCHS/vpp12.png', 'VPP - DCHS', 100);

-- Xác nhận insert thành công
SELECT 'Đã insert thành công 12 sản phẩm VPP - DCHS!' AS status;

-- Insert 7 sản phẩm Sách Giáo Khoa 2025 vào bảng products

INSERT INTO products (name, price, image_url, category, stock) VALUES
('Hộp 20 Bút Gel Buttersmooth B 0.5 mm - Thiên Long GELB-034', 75000, '/images/SGK/school1.png', 'Sách Giáo Khoa', 100),
('Sổ Lò Xo Read me A6 - Kẻ Ngang 80gsm - WeWrite 423', 27500, '/images/SGK/school2.png', 'Sách Giáo Khoa', 100),
('Bút Gel Buttersmooth B 0.5 mm - Thiên Long GELB-033', 4750, '/images/SGK/school3.png', 'Sách Giáo Khoa', 100),
('Hộp 20 Bút Gel Buttersmooth B 0.5 mm - Thiên Long GELB-033 (Mẫu 1)', 95000, '/images/SGK/school4.png', 'Sách Giáo Khoa', 100),
('Lịch Bloc Đại Đặc Biệt Màng Co - Chủ Đề Trà & Thư Pháp - Mã Đả...', 67500, '/images/SGK/school5.png', 'Sách Giáo Khoa', 100),
('Lịch Bloc Siêu Cực Đại - Chủ Đề Phong Cảnh Việt Nam - Mã Đảo...', 302500, '/images/SGK/school6.png', 'Sách Giáo Khoa', 100),
('Hộp 20 Bút Gel Buttersmooth B 0.5 mm - Thiên Long GELB-033 (Mẫu 2)', 95000, '/images/SGK/school7.png', 'Sách Giáo Khoa', 100);

-- Xác nhận insert thành công
SELECT 'Đã insert thành công 7 sản phẩm Sách Giáo Khoa 2025!' AS status;

-- Insert 12 sản phẩm VPP - Dụng Cụ Học Sinh vào bảng products

INSERT INTO products (name, price, image_url, category, stock) VALUES
('Lịch Bàn Thị Trấn Mèo 2026', 54000, '/images/VPP/vpp1.png', 'VPP - Dụng Cụ Học Sinh', 100),
('Hộp 20 Tuýp Màu Vẽ Acrylic 20 ml - Colormate 114919', 145800, '/images/VPP/vpp2.png', 'VPP - Dụng Cụ Học Sinh', 100),
('Hộp 16 Chai Màu Vẽ Pastel Acrylic 20 ml - Colormate 115046', 154800, '/images/VPP/vpp3.png', 'VPP - Dụng Cụ Học Sinh', 100),
('Vỉ 10 Bút Chi HB Zootopia - iiGEN YZ360035', 45000, '/images/VPP/vpp4.png', 'VPP - Dụng Cụ Học Sinh', 100),
('Sổ Lò Xo Made With Love A6 - Kẻ Ngang - 210 Trang', 32000, '/images/VPP/vpp5.png', 'VPP - Dụng Cụ Học Sinh', 100),
('Bút Chi Bấm 0.5 mm Kèm Tẩy Và Ngòi Zootopia - iiGEN YZ360050 (Var 1)', 45000, '/images/VPP/vpp6.png', 'VPP - Dụng Cụ Học Sinh', 100),
('Bút Chi Bấm 0.5 mm Kèm Tẩy Và Ngòi Zootopia - iiGEN YZ360050 (Var 2)', 45000, '/images/VPP/vpp7.png', 'VPP - Dụng Cụ Học Sinh', 100),
('Bút Chi Bấm 0.5 mm Zootopia - iiGEN YZ360050 (Var 1)', 27000, '/images/VPP/vpp8.png', 'VPP - Dụng Cụ Học Sinh', 100),
('Ngòi Bút Chi 2B 0.5 mm Zootopia - iiGEN YZ360052 (Var 1)', 18000, '/images/VPP/vpp9.png', 'VPP - Dụng Cụ Học Sinh', 100),
('Bút Chi Bấm 0.5 mm Zootopia - iiGEN YZ360050 (Var 2)', 27000, '/images/VPP/vpp10.png', 'VPP - Dụng Cụ Học Sinh', 100),
('Kéo Học Sinh Zootopia - iiGEN YZ360056 - Nick Wilde', 27000, '/images/VPP/vpp11.png', 'VPP - Dụng Cụ Học Sinh', 100),
('Ngòi Bút Chi 2B 0.5 mm Zootopia - iiGEN YZ360052 (Var 2)', 18000, '/images/VPP/vpp12.png', 'VPP - Dụng Cụ Học Sinh', 100);

-- Xác nhận insert thành công
SELECT 'Đã insert thành công 12 sản phẩm VPP - Dụng Cụ Học Sinh!' AS status;

-- Insert 8 sản phẩm Đồ Chơi Theo Thương Hiệu vào bảng products

INSERT INTO products (name, price, image_url, category, stock) VALUES
('Đồ Chơi Mô Hình Xe Tải Chở - Vecto VT18B', 179100, '/images/DCTTH/toy1.png', 'Đồ Chơi Theo Thương Hiệu', 100),
('Đồ Chơi Mô Hình Xe Chở Xăng Chạy Trốn - Vecto', 413100, '/images/DCTTH/toy2.png', 'Đồ Chơi Theo Thương Hiệu', 100),
('Đồ Chơi Mô Hình Xe Cứu Hộ Có Đèn Và Âm Thanh - Vecto', 413100, '/images/DCTTH/toy3.png', 'Đồ Chơi Theo Thương Hiệu', 100),
('Đồ Chơi Mô Hình Xe Cứu Hỏa Có Đèn Và Âm Thanh - Vecto', 413100, '/images/DCTTH/toy4.png', 'Đồ Chơi Theo Thương Hiệu', 100),
('Đồ Chơi Mô Hình Xe Rác Có Đèn Và Âm Thanh - Vecto', 413100, '/images/DCTTH/toy5.png', 'Đồ Chơi Theo Thương Hiệu', 100),
('Đồ Chơi Mô Hình Xe Cần Cầu Có Đèn Và Âm Thanh - Vecto', 413100, '/images/DCTTH/toy6.png', 'Đồ Chơi Theo Thương Hiệu', 100),
('Đồ Chơi Mô Hình Xe Trộn Xi Măng Có Đèn Và Âm Thanh - Vecto', 413100, '/images/DCTTH/toy7.png', 'Đồ Chơi Theo Thương Hiệu', 100),
('Đồ Chơi Robot Biến Hình Thành Máy Bay Trực Thăng - Vecto', 269100, '/images/DCTTH/toy8.png', 'Đồ Chơi Theo Thương Hiệu', 100);

-- Xác nhận insert thành công
SELECT 'Đã insert thành công 8 sản phẩm Đồ Chơi Theo Thương Hiệu!' AS status;

-- Insert 8 sản phẩm Bách Hóa Online - Lưu Niệm vào bảng products

INSERT INTO products (name, price, image_url, category, stock) VALUES
('Ly Sứ Noel Có Nắp - TP86 - Mẫu 2', 144900, '/images/BHO/gift1.png', 'Bách Hóa Online - Lưu Niệm', 100),
('Ly Sứ Noel Có Nắp - TP86 - Mẫu 1', 144900, '/images/BHO/gift2.png', 'Bách Hóa Online - Lưu Niệm', 100),
('Cài Tóc Giáng Sinh - Bu Bu BB7350 - Ông Già Noel', 14400, '/images/BHO/gift3.png', 'Bách Hóa Online - Lưu Niệm', 100),
('Cài Tóc Giáng Sinh - Bu Bu BB7350 - Sừng Tuần Lộc', 14400, '/images/BHO/gift4.png', 'Bách Hóa Online - Lưu Niệm', 100),
('Túi Dây Rút Zootopia - iiGEN 310003 - Judy Hopps', 234000, '/images/BHO/gift5.png', 'Bách Hóa Online - Lưu Niệm', 100),
('Túi Dây Rút Zootopia - iiGEN 310003 - Nick Wilde (Mẫu 1)', 234000, '/images/BHO/gift6.png', 'Bách Hóa Online - Lưu Niệm', 100),
('Túi Dây Rút Zootopia - iiGEN 310003 - Nick Wilde (Mẫu 2)', 234000, '/images/BHO/gift7.png', 'Bách Hóa Online - Lưu Niệm', 100),
('Túi Dây Rút Zootopia - iiGEN 310003 - Flash', 234000, '/images/BHO/gift8.png', 'Bách Hóa Online - Lưu Niệm', 100);

-- Xác nhận insert thành công
SELECT 'Đã insert thành công 8 sản phẩm Bách Hóa Online - Lưu Niệm!' AS status;