CREATE DATABASE Academy;
GO
USE Academy;
GO
CREATE TABLE Students(
    Id VARCHAR(20) NOT NULL,
    Fullname NVARCHAR(50) NOT NULL,
    Mark FLOAT NOT NULL,
    Birthday DATE NOT NULL,
    PRIMARY KEY(Id)
);
GO
INSERT INTO Students (Id, Fullname, Mark, Birthday) VALUES
('SV001', N'Nguyễn Văn A', 8.5, '2000-05-15'),
('SV002', N'Trần Thị B', 9.0, '1999-12-20'),
('SV003', N'Lê Văn C', 7.5, '2001-03-10');
GO
SELECT * FROM Students;
GO