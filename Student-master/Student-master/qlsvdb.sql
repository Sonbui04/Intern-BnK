-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th7 08, 2025 lúc 06:20 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `qlsvdb`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `canhcao`
--

CREATE TABLE `canhcao` (
  `id` int(11) NOT NULL,
  `ten_canh_cao` varchar(100) NOT NULL,
  `mo_ta` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `canhcao`
--

INSERT INTO `canhcao` (`id`, `ten_canh_cao`, `mo_ta`) VALUES
(1, 'Cảnh cáo học vụ', 'Vi phạm quy chế học tập, nợ môn'),
(2, 'Vi phạm nội quy', 'Vi phạm các quy định của nhà trường'),
(3, 'Cảnh cáo đạo đức', 'Vi phạm đạo đức sinh viên'),
(4, 'cảnh cáo nội vụ học sinh', 'cảnh cáo nội vụ học sinh');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `lop`
--

CREATE TABLE `lop` (
  `id` int(11) NOT NULL,
  `ma_lop` varchar(20) NOT NULL,
  `ten_lop` varchar(50) NOT NULL,
  `nam_hoc` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `lop`
--

INSERT INTO `lop` (`id`, `ma_lop`, `ten_lop`, `nam_hoc`) VALUES
(1, 'DHKTPM15A', 'Kỹ thuật phần mềm 15A', 2023),
(2, 'DHKTPM15B', 'Kỹ 15B', 2023),
(3, 'DHKTPM16', 'Kỹ thuật phần mềm 12', 2024),
(4, 'DHTCDN01', 'Công nghệ đường bộ 01', 2023),
(5, '1', 'toán', NULL),
(6, '2', 'văn', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `role_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `roles`
--

INSERT INTO `roles` (`id`, `role_name`) VALUES
(1, 'ADMIN'),
(3, 'GIAO_VIEN'),
(2, 'SV');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sinhvien`
--

CREATE TABLE `sinhvien` (
  `id` varchar(20) NOT NULL,
  `hoten` varchar(100) NOT NULL,
  `gioitinh` varchar(10) DEFAULT NULL,
  `ngaysinh` date DEFAULT NULL,
  `diachi` varchar(255) DEFAULT NULL,
  `sdt` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `lop_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sinhvien`
--

INSERT INTO `sinhvien` (`id`, `hoten`, `gioitinh`, `ngaysinh`, `diachi`, `sdt`, `email`, `lop_id`) VALUES
('SV001', 'Nguyễn Văn A', 'Nam', '2001-01-01', '123 Đường ABC, Hà Nội', '0912345678', 'a@gmail.com', 3),
('SV002', 'Trần Thị B', 'Nữ', '2002-02-02', '456 Phố XYZ, Nam Định', '0987654321', 'b@gmail.com', 2),
('SV003', 'Lê Văn C', 'Nam', '2001-05-10', '789 Ngõ QWE, Hải Phòng', '0901122334', 'c@gmail.com', 1),
('SV004', 'Phạm Thị D', 'Nữ', '2003-03-15', '101 Đường MNO, Đà Nẵng', '0977889900', 'd@gmail.com', 2),
('sv123', 'bùi văn sơn', 'Nam', '2004-12-12', 'nga sơn', '123', 's@gmail.com', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sinhvien_canhcao`
--

CREATE TABLE `sinhvien_canhcao` (
  `sinhvien_id` varchar(20) NOT NULL,
  `canhcao_id` int(11) NOT NULL,
  `ngay_canh_cao` date NOT NULL,
  `hoc_ky` varchar(20) DEFAULT NULL,
  `ly_do` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sinhvien_canhcao`
--

INSERT INTO `sinhvien_canhcao` (`sinhvien_id`, `canhcao_id`, `ngay_canh_cao`, `hoc_ky`, `ly_do`) VALUES
('SV001', 1, '2025-07-08', '1', 'chậm tiền'),
('SV002', 1, '2024-03-05', 'Kỳ 2 - 2023-2024', 'Nợ 2 môn giải tích'),
('SV002', 1, '2025-07-01', '1', 'chậm tiền'),
('SV002', 1, '2025-07-08', '1', 'chưa đóng tiền học');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `username`, `password_hash`, `full_name`, `email`, `role_id`) VALUES
(1, 'admin', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', 'Quản trị viên', 'admin@example.com', 1),
(2, 'sv', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', 'son', 's', 2);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `canhcao`
--
ALTER TABLE `canhcao`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ten_canh_cao` (`ten_canh_cao`);

--
-- Chỉ mục cho bảng `lop`
--
ALTER TABLE `lop`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ma_lop` (`ma_lop`);

--
-- Chỉ mục cho bảng `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `role_name` (`role_name`);

--
-- Chỉ mục cho bảng `sinhvien`
--
ALTER TABLE `sinhvien`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_sinhvien_lop` (`lop_id`);

--
-- Chỉ mục cho bảng `sinhvien_canhcao`
--
ALTER TABLE `sinhvien_canhcao`
  ADD PRIMARY KEY (`sinhvien_id`,`canhcao_id`,`ngay_canh_cao`),
  ADD KEY `fk_sv_cc_cc` (`canhcao_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `fk_user_role` (`role_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `canhcao`
--
ALTER TABLE `canhcao`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `lop`
--
ALTER TABLE `lop`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `sinhvien`
--
ALTER TABLE `sinhvien`
  ADD CONSTRAINT `fk_sinhvien_lop` FOREIGN KEY (`lop_id`) REFERENCES `lop` (`id`);

--
-- Các ràng buộc cho bảng `sinhvien_canhcao`
--
ALTER TABLE `sinhvien_canhcao`
  ADD CONSTRAINT `fk_sv_cc_cc` FOREIGN KEY (`canhcao_id`) REFERENCES `canhcao` (`id`),
  ADD CONSTRAINT `fk_sv_cc_sv` FOREIGN KEY (`sinhvien_id`) REFERENCES `sinhvien` (`id`);

--
-- Các ràng buộc cho bảng `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
