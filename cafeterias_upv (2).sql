-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Mar 27, 2025 at 02:44 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cafeterias_upv`
--

-- --------------------------------------------------------

--
-- Table structure for table `axelcafe`
--

CREATE TABLE `axelcafe` (
  `id` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `en_stock` int(11) NOT NULL,
  `t_preparacion` int(11) NOT NULL,
  `categoria` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `axelcafe`
--

INSERT INTO `axelcafe` (`id`, `nombre`, `precio`, `en_stock`, `t_preparacion`, `categoria`) VALUES
('2230NCLZ', 'Oreo', 20.00, 2, 0, 'galletas'),
('5EZwtKYX', 'Dorilocos', 17.00, 90, 0, 'galletas');

-- --------------------------------------------------------

--
-- Table structure for table `bilingue`
--

CREATE TABLE `bilingue` (
  `id` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `en_stock` int(11) NOT NULL,
  `t_preparacion` int(11) NOT NULL,
  `categoria` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bilingue`
--

INSERT INTO `bilingue` (`id`, `nombre`, `precio`, `en_stock`, `t_preparacion`, `categoria`) VALUES
('JldPJ4tm', 'Pizza', 80.00, 100, 60, 'galletas');

-- --------------------------------------------------------

--
-- Table structure for table `fide`
--

CREATE TABLE `fide` (
  `id` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `en_stock` int(11) NOT NULL,
  `t_preparacion` int(11) NOT NULL,
  `categoria` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `fide`
--

INSERT INTO `fide` (`id`, `nombre`, `precio`, `en_stock`, `t_preparacion`, `categoria`) VALUES
('HNsTlQdF', 'Oreo', 20.00, 15, 0, 'galletas');

-- --------------------------------------------------------

--
-- Table structure for table `jaguares`
--

CREATE TABLE `jaguares` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `precio` decimal(10,2) DEFAULT NULL,
  `en_stock` int(11) DEFAULT NULL,
  `t_preparacion` int(11) DEFAULT NULL,
  `categoria` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jaguares`
--

INSERT INTO `jaguares` (`id`, `nombre`, `precio`, `en_stock`, `t_preparacion`, `categoria`) VALUES
(1, 'Manzanita 600ml', 19.00, 29, 0, 'Bebidas'),
(2, 'Escuis Fresa 600ml', 19.00, 45, 0, 'Bebidas'),
(3, 'Escuis Piña 600ml', 19.00, 15, 0, 'Bebidas'),
(4, 'Agua Ciel Litro', 15.00, 32, 0, 'Bebidas'),
(5, 'Agua Epura Litro', 18.00, 20, 0, 'Bebidas'),
(6, 'Peñafiel 400ml', 19.00, 25, 0, 'Bebidas'),
(7, 'Yoghurt Lala 300grs', 18.00, 21, 0, 'Bebidas'),
(8, 'Yoghurt Danone 300grs', 16.00, 10, 0, 'Bebidas'),
(9, 'Coca 600ml', 20.00, 50, 0, 'Bebidas'),
(10, 'Coca Sin Azucar 600ml', 18.00, 17, 0, 'Bebidas'),
(11, 'Coca Light 600ml', 20.00, 34, 0, 'Bebidas'),
(12, 'Mirinda 600ml', 19.00, 26, 0, 'Bebidas'),
(13, 'Sprite 600ml', 19.00, 38, 0, 'Bebidas'),
(14, 'Flautas Chicas', 15.00, 13, 10, 'Comida'),
(15, 'Flautas Grandes', 50.00, 21, 12, 'Comida'),
(16, 'Gorditas', 12.00, 9, 9, 'Comida'),
(17, 'Papas gajo media orden', 20.00, 9, 8, 'Comida'),
(18, 'Papas gajo orden', 35.00, 15, 10, 'Comida'),
(19, 'Sope Grande', 40.00, 8, 12, 'Comida'),
(20, 'Burritos (Pizza, Frijol)', 15.00, 22, 7, 'Comida'),
(21, 'Boneless media orden', 80.00, 15, 15, 'Comida'),
(22, 'Boneless orden', 140.00, 16, 20, 'Comida'),
(23, 'Tix-Tix', 8.00, 1, 0, 'Dulces'),
(24, 'Paleta Tix-Tix', 5.00, 7, 0, 'Dulces'),
(25, 'Oreo', 20.00, 2, 0, 'Galletas'),
(26, 'Sponch', 20.00, 18, 0, 'Galletas'),
(27, 'TrikiTrakets', 20.00, 24, 0, 'Dulces'),
(28, 'Marias', 20.00, 7, 0, 'Galletas'),
(29, 'Emperador Chocolate', 20.00, 6, 0, 'Galletas'),
(30, 'Emperador Vainilla', 20.00, 13, 0, 'Galletas'),
(31, 'Emperador Combinado', 20.00, 29, 0, 'Galletas'),
(32, 'Emperador Senzo', 20.00, 25, 0, 'Galletas'),
(33, 'Emperador Nuez', 20.00, 4, 0, 'Galletas'),
(34, 'Emperador Nocturnas', 20.00, 5, 0, 'Galletas'),
(35, 'Picafresa', 2.00, 12, 0, 'Dulces'),
(36, 'Chips Habanero', 20.00, 13, 0, 'Sabritas'),
(37, 'Chettos Torciditos', 14.00, 21, 0, 'Sabritas'),
(38, 'Chettos Colmillo', 14.00, 13, 0, 'Sabritas'),
(39, 'Chettos Flammin Hot', 14.00, 18, 0, 'Sabritas'),
(40, 'Chettos Puff', 14.00, 24, 0, 'Sabritas'),
(41, 'Doritos Clasicos', 18.00, 17, 0, 'Sabritas'),
(42, 'Doritos Incognita', 18.00, 12, 0, 'Sabritas'),
(43, 'Doritos Pizzerola', 18.00, 19, 0, 'Sabritas'),
(44, 'Ruffles', 18.00, 13, 0, 'Sabritas'),
(45, 'Chips Fuego', 20.00, 11, 0, 'Sabritas'),
(46, 'Chips Sal', 20.00, 8, 0, 'Sabritas'),
(47, 'Rockaleta', 8.00, 29, 0, 'Dulces'),
(48, 'Trident', 3.00, 20, 0, 'Dulces');

-- --------------------------------------------------------

--
-- Table structure for table `pedidos_axelcafe`
--

CREATE TABLE `pedidos_axelcafe` (
  `num_pedido` int(11) NOT NULL,
  `nom_cliente` varchar(100) NOT NULL,
  `pedido` text NOT NULL,
  `total_pago` decimal(10,2) NOT NULL,
  `tiempo_estimado` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pedidos_axelcafe`
--

INSERT INTO `pedidos_axelcafe` (`num_pedido`, `nom_cliente`, `pedido`, `total_pago`, `tiempo_estimado`) VALUES
(1, 'Miguel', '- Oreo\n', 20.00, 0),
(2, 'Azel', '', 0.00, 0),
(3, 'Chuyito', '- Oreo\n', 20.00, 0),
(4, 'hola', '- Oreo\n', 20.00, 0);

-- --------------------------------------------------------

--
-- Table structure for table `pedidos_bilingue`
--

CREATE TABLE `pedidos_bilingue` (
  `num_pedido` int(11) NOT NULL,
  `nom_cliente` varchar(100) NOT NULL,
  `pedido` text NOT NULL,
  `total_pago` decimal(10,2) NOT NULL,
  `tiempo_estimado` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pedidos_fide`
--

CREATE TABLE `pedidos_fide` (
  `num_pedido` int(11) NOT NULL,
  `nom_cliente` varchar(100) NOT NULL,
  `pedido` text NOT NULL,
  `total_pago` decimal(10,2) NOT NULL,
  `tiempo_estimado` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pedidos_jaguares`
--

CREATE TABLE `pedidos_jaguares` (
  `num_pedido` int(11) NOT NULL,
  `nom_cliente` varchar(100) NOT NULL,
  `pedido` text NOT NULL,
  `total_pago` decimal(10,2) NOT NULL,
  `tiempo_estimado` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pedidos_jaguares`
--

INSERT INTO `pedidos_jaguares` (`num_pedido`, `nom_cliente`, `pedido`, `total_pago`, `tiempo_estimado`) VALUES
(1, 'Zamudio', '- Mirinda 600ml\n- Papas gajo media orden\n- Emperador Senzo\n', 59.00, 16),
(2, 'Zamudio2', '- Emperador Combinado\n', 20.00, 2),
(3, 'jared', '- Chettos Colmillo\n', 14.00, 2),
(4, 'fide', '- Sprite 600ml\n- Oreo\n', 39.00, 7),
(5, 'hola', '- Paleta Tix-Tix\n', 5.00, 0),
(6, 'Mike3', '- Gorditas\n', 12.00, 9),
(7, 'Mike4', '- Picafresa\n', 2.00, 0),
(8, 'Choi', '- Boneless orden [sinSalsa]\n', 140.00, 20),
(9, 'P1', '- Oreo\n', 20.00, 0),
(10, 'P2', '- Oreo\n', 20.00, 0),
(11, 'P4', '- Oreo\n', 20.00, 0),
(12, 'P7', '- Oreo\n', 20.00, 0),
(13, 'P9', '- Oreo\n- Flautas Chicas [salsaVerde]\n', 35.00, 10),
(14, 'D1', '- Oreo\n', 20.00, 0),
(15, 'D2', '- Oreo\n', 20.00, 0),
(16, 'ZamudioPEPE', '- Oreo\n- Yoghurt Lala 300grs\n', 38.00, 0),
(17, 'Pusy', '- Flautas Grandes [huevoConPapas]\n', 50.00, 12),
(18, 'Choi', '- Flautas Grandes [huevoConPapas]\n', 50.00, 12),
(19, 'My name', '- Ruffles\n', 18.00, 0),
(20, 'Llli', '- Chettos Torciditos\n', 14.00, 0),
(21, 'Alec', '- Marias\n', 20.00, 0),
(22, 'Alex', '- Oreo\n- Marias\n', 40.00, 0),
(23, 'Alec2', '- Sponch\n- Marias\n', 40.00, 0),
(24, 'Alec3', '- Manzanita 600ml\n- Escuis Fresa 600ml\n', 38.00, 0),
(25, 'Alec4', '- TrikiTrakets\n- Rockaleta\n', 28.00, 0),
(26, 'Josefina', '- Chips Habanero\n- Chettos Colmillo\n', 34.00, 0),
(27, 'Alejandro', '- Chips Habanero\n- Chettos Colmillo\n', 34.00, 0),
(28, 'Axel', '- Emperador Chocolate\n- Emperador Senzo\n- Emperador Nocturnas\n- Emperador Nuez\n', 80.00, 0),
(29, 'jared24', '- Emperador Vainilla\n- Emperador Senzo\n', 40.00, 0),
(30, 'Choi', '- Sprite 600ml\n- Coca Sin Azucar 600ml\n', 37.00, 0),
(31, 'Jesus', '- Boneless orden [22]\n- Boneless orden [buffalo]\n- Chips Sal\n', 300.00, 40),
(32, 'Jesus', '- Boneless orden [bbq]\n- Coca Light 600ml\n', 160.00, 20),
(33, 'Jesus 3', '- Gorditas [picadillo]\n', 12.00, 9),
(34, 'prueba1', '- Manzanita 600ml\n', 19.00, 0);

-- --------------------------------------------------------

--
-- Table structure for table `pedidos_sandovalcafe`
--

CREATE TABLE `pedidos_sandovalcafe` (
  `num_pedido` int(11) NOT NULL,
  `nom_cliente` varchar(100) NOT NULL,
  `pedido` text NOT NULL,
  `total_pago` decimal(10,2) NOT NULL,
  `tiempo_estimado` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sandovalcafe`
--

CREATE TABLE `sandovalcafe` (
  `id` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `en_stock` int(11) NOT NULL,
  `t_preparacion` int(11) NOT NULL,
  `categoria` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `axelcafe`
--
ALTER TABLE `axelcafe`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bilingue`
--
ALTER TABLE `bilingue`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `fide`
--
ALTER TABLE `fide`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `jaguares`
--
ALTER TABLE `jaguares`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pedidos_axelcafe`
--
ALTER TABLE `pedidos_axelcafe`
  ADD PRIMARY KEY (`num_pedido`);

--
-- Indexes for table `pedidos_bilingue`
--
ALTER TABLE `pedidos_bilingue`
  ADD PRIMARY KEY (`num_pedido`);

--
-- Indexes for table `pedidos_fide`
--
ALTER TABLE `pedidos_fide`
  ADD PRIMARY KEY (`num_pedido`);

--
-- Indexes for table `pedidos_jaguares`
--
ALTER TABLE `pedidos_jaguares`
  ADD PRIMARY KEY (`num_pedido`);

--
-- Indexes for table `pedidos_sandovalcafe`
--
ALTER TABLE `pedidos_sandovalcafe`
  ADD PRIMARY KEY (`num_pedido`);

--
-- Indexes for table `sandovalcafe`
--
ALTER TABLE `sandovalcafe`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jaguares`
--
ALTER TABLE `jaguares`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT for table `pedidos_axelcafe`
--
ALTER TABLE `pedidos_axelcafe`
  MODIFY `num_pedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `pedidos_bilingue`
--
ALTER TABLE `pedidos_bilingue`
  MODIFY `num_pedido` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pedidos_fide`
--
ALTER TABLE `pedidos_fide`
  MODIFY `num_pedido` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pedidos_jaguares`
--
ALTER TABLE `pedidos_jaguares`
  MODIFY `num_pedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `pedidos_sandovalcafe`
--
ALTER TABLE `pedidos_sandovalcafe`
  MODIFY `num_pedido` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
