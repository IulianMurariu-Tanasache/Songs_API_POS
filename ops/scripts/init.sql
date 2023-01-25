create user if not exists 'dbadmin'@'%' identified by 'dbadmin';
grant all on *.* to 'dbadmin'@'%' with grant option;

CREATE DATABASE IF NOT EXISTS spotify_db;

USE spotify_db;
CREATE TABLE `artists` (
  `uuid` varchar(255) NOT NULL,
  `active` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `music` (
  `id` int NOT NULL AUTO_INCREMENT,
  `genre` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `release_year` int DEFAULT NULL,
  `type` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `music_artists` (
  `id_music` int NOT NULL,
  `id_artist` varchar(255) NOT NULL,
  PRIMARY KEY (`id_music`,`id_artist`),
  KEY `FKmkekkut8lg3x0q54j852avk4n` (`id_artist`),
  CONSTRAINT `FKmkekkut8lg3x0q54j852avk4n` FOREIGN KEY (`id_artist`) REFERENCES `artists` (`uuid`),
  CONSTRAINT `FKpywaxfvc86k041jvdr9jiate8` FOREIGN KEY (`id_music`) REFERENCES `music` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE DATABASE IF NOT EXISTS spotify_users;
USE spotify_users;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `token` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users_roles` (
  `id_user` int NOT NULL,
  `id_role` int NOT NULL,
  PRIMARY KEY (`id_user`,`id_role`),
  KEY `FK3avenccqsoqwrfur1hb8mpbrw` (`id_role`),
  CONSTRAINT `FK3avenccqsoqwrfur1hb8mpbrw` FOREIGN KEY (`id_role`) REFERENCES `roles` (`id`),
  CONSTRAINT `FK6ywr92flw5416dup8uc2egb83` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `spotify_users`.`roles` (`id`, `role`) VALUES ('1', 'ROLE_CLIENT');
INSERT INTO `spotify_users`.`roles` (`id`, `role`) VALUES ('2', 'ROLE_CMANAGER');
INSERT INTO `spotify_users`.`roles` (`id`, `role`) VALUES ('3', 'ROLE_ARTIST');
INSERT INTO `spotify_users`.`roles` (`id`, `role`) VALUES ('4', 'ROLE_ADMIN');

INSERT INTO `spotify_users`.`users`(`id`,`password`,`username`) VALUES('1', '$2a$10$JqjLYdP2q8O3AD3eWr2yZuZyA6iRPHQ.Qnksud/Bb9.gdQ2mxj.we', 'admin');

INSERT INTO `spotify_users`.`users_roles` (`id_user`,`id_role`) VALUES (1,1);
INSERT INTO `spotify_users`.`users_roles` (`id_user`,`id_role`) VALUES (1,2);
INSERT INTO `spotify_users`.`users_roles` (`id_user`,`id_role`) VALUES (1,3);
INSERT INTO `spotify_users`.`users_roles` (`id_user`,`id_role`) VALUES (1,4);
