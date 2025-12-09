CREATE DATABASE ProductDB;
GO

USE ProductDB;
GO

-- Tạo bảng SanPham
CREATE TABLE SanPham (
    maSanPham VARCHAR(50) PRIMARY KEY,
    tenSanPham NVARCHAR(255) NOT NULL,
    donGia DECIMAL(10,2) NOT NULL,
    hanSuDung DATE NOT NULL,
    hinhAnh NVARCHAR(500),
    nguonGoc NVARCHAR(20) CHECK (nguonGoc IN ('Cao cấp', 'Bình dân'))
);
GO