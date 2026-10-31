-- ========================================================
-- RELIEFCAMP – Disaster Relief Camp Resource Tracker
-- MySQL Database Schema (For Manual DB Setup / Viva Demo)
-- ========================================================

CREATE DATABASE IF NOT EXISTS `reliefcamp`;
USE `reliefcamp`;

-- 1. Camps Table
CREATE TABLE IF NOT EXISTS `camps` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `location` VARCHAR(150) NOT NULL,
    `capacity` INT NOT NULL,
    `current_occupancy` INT NOT NULL DEFAULT 0,
    `status` VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Supplies Table (Inventory)
CREATE TABLE IF NOT EXISTS `supplies` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `camp_id` BIGINT NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(50) NOT NULL,
    `quantity` INT NOT NULL DEFAULT 0,
    `unit` VARCHAR(30) NOT NULL,
    `received_date` DATE NOT NULL,
    CONSTRAINT `fk_supplies_camp` FOREIGN KEY (`camp_id`) REFERENCES `camps` (`id`) ON DELETE CASCADE
);

-- 3. Families Table
CREATE TABLE IF NOT EXISTS `families` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `camp_id` BIGINT NOT NULL,
    `family_head` VARCHAR(100) NOT NULL,
    `member_count` INT NOT NULL,
    `phone` VARCHAR(20),
    `check_in_date` DATE NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'CHECKED_IN',
    CONSTRAINT `fk_families_camp` FOREIGN KEY (`camp_id`) REFERENCES `camps` (`id`) ON DELETE CASCADE
);

-- 4. Distributions Table
CREATE TABLE IF NOT EXISTS `distributions` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `camp_id` BIGINT NOT NULL,
    `family_id` BIGINT NOT NULL,
    `supply_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `distribution_date` DATE NOT NULL,
    `remarks` VARCHAR(255),
    `status` VARCHAR(30) NOT NULL DEFAULT 'COMPLETED',
    CONSTRAINT `fk_dist_camp` FOREIGN KEY (`camp_id`) REFERENCES `camps` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_dist_family` FOREIGN KEY (`family_id`) REFERENCES `families` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_dist_supply` FOREIGN KEY (`supply_id`) REFERENCES `supplies` (`id`) ON DELETE CASCADE
);
