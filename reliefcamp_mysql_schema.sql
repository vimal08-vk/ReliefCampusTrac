-- ========================================================
-- ReliefCamp - Disaster Relief Camp Resource Tracker
-- MySQL Database Schema & Sample Initial Data
-- ========================================================

CREATE DATABASE IF NOT EXISTS `reliefcamp` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `reliefcamp`;

-- 1. Relief Camps Table
CREATE TABLE IF NOT EXISTS `camps` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `location` VARCHAR(150) NOT NULL,
    `capacity` INT NOT NULL,
    `current_occupancy` INT NOT NULL DEFAULT 0,
    `status` VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. Relief Supplies Table
CREATE TABLE IF NOT EXISTS `supplies` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(50) NOT NULL,
    `quantity` INT NOT NULL,
    `unit` VARCHAR(30) NOT NULL,
    `received_date` DATE NOT NULL,
    `camp_id` BIGINT NOT NULL,
    FOREIGN KEY (`camp_id`) REFERENCES `camps`(`id`) ON DELETE CASCADE
);

-- 3. Affected Families Table
CREATE TABLE IF NOT EXISTS `families` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `family_head` VARCHAR(100) NOT NULL,
    `member_count` INT NOT NULL,
    `phone` VARCHAR(20),
    `check_in_date` DATE NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'CHECKED_IN',
    `camp_id` BIGINT NOT NULL,
    FOREIGN KEY (`camp_id`) REFERENCES `camps`(`id`) ON DELETE RESTRICT
);

-- 4. Supply Distribution Table
CREATE TABLE IF NOT EXISTS `distributions` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `camp_id` BIGINT NOT NULL,
    `family_id` BIGINT NOT NULL,
    `supply_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `distribution_date` DATE NOT NULL,
    `remarks` VARCHAR(255),
    `status` VARCHAR(30) NOT NULL DEFAULT 'COMPLETED',
    FOREIGN KEY (`camp_id`) REFERENCES `camps`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`family_id`) REFERENCES `families`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`supply_id`) REFERENCES `supplies`(`id`) ON DELETE RESTRICT
);

-- Sample Data Demonstration Script (Matches College Assessment Specification)
INSERT INTO `camps` (`id`, `name`, `location`, `capacity`, `current_occupancy`, `status`) VALUES
(1, 'Pollachi Relief Camp', 'Pollachi Govt Higher Secondary School', 100, 12, 'ACTIVE'),
(2, 'Coimbatore Central Shelter', 'VOC Ground Indoor Stadium, Coimbatore', 150, 0, 'ACTIVE'),
(3, 'Valparai Hill Shelter', 'Valparai Town Community Hall', 60, 0, 'ACTIVE')
ON DUPLICATE KEY UPDATE `id`=`id`;

INSERT INTO `supplies` (`id`, `name`, `type`, `quantity`, `unit`, `received_date`, `camp_id`) VALUES
(1, 'Food Packets', 'Food', 480, 'Packets', CURDATE(), 1),
(2, 'Drinking Water Bottles (1L)', 'Water', 890, 'Bottles', CURDATE(), 1),
(3, 'Woolen Blankets', 'Blankets', 120, 'Pieces', CURDATE(), 1),
(4, 'First Aid Medicine Kits', 'Medicines', 40, 'Kits', CURDATE(), 1)
ON DUPLICATE KEY UPDATE `id`=`id`;

INSERT INTO `families` (`id`, `family_head`, `member_count`, `phone`, `check_in_date`, `status`, `camp_id`) VALUES
(1, 'Kumar', 5, '9876543210', CURDATE(), 'CHECKED_IN', 1),
(2, 'Priya Lakshmi', 4, '9845123456', CURDATE(), 'CHECKED_IN', 1),
(3, 'Ramesh Chandran', 3, '9789012345', CURDATE(), 'CHECKED_IN', 1)
ON DUPLICATE KEY UPDATE `id`=`id`;

INSERT INTO `distributions` (`id`, `camp_id`, `family_id`, `supply_id`, `quantity`, `distribution_date`, `remarks`, `status`) VALUES
(1, 1, 1, 1, 20, CURDATE(), 'Initial emergency dinner packets', 'COMPLETED'),
(2, 1, 2, 2, 10, CURDATE(), 'Daily clean drinking water bottles', 'COMPLETED')
ON DUPLICATE KEY UPDATE `id`=`id`;
