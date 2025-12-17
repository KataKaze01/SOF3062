CREATE DATABASE J6Exam;
GO
USE J6Exam;
GO

CREATE TABLE Products (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    image NVARCHAR(500),
    description NVARCHAR(MAX)
);
GO
INSERT INTO Products (name, price, image, description)
VALUES 
('Laptop Dell Inspiron', 12000000.00, 'https://example.com/laptop.jpg', 'Laptop văn phòng, cấu hình ổn định'),
('iPhone 15 Pro', 25000000.00, 'https://example.com/iphone.jpg', 'Điện thoại cao cấp, camera xuất sắc'),
('Tai nghe Bluetooth Sony', 1500000.00, 'https://example.com/headphone.jpg', 'Âm thanh chất lượng, pin trâu'),
('Máy in HP LaserJet', 3500000.00, 'https://example.com/printer.jpg', 'In nhanh, tiết kiệm mực');
GO